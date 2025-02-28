package moe.takanashihoshino.nyaniduserserver.server.web.User;
//注册

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moe.takanashihoshino.nyaniduserserver.utils.ErrUtils.ErrRes;
import moe.takanashihoshino.nyaniduserserver.utils.ErrUtils.SJson;
import moe.takanashihoshino.nyaniduserserver.utils.RedisUtils.RedisService;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.AccountsRepository;
import moe.takanashihoshino.nyaniduserserver.utils.EmailHelper.EmailService;
import moe.takanashihoshino.nyaniduserserver.utils.OtherUtils;
import moe.takanashihoshino.nyaniduserserver.utils.UUIDHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/zako/v1/register")
public class RegisterApi {

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisService redisService;

    @Value("${NyanidSetting.encryptionKey}")
    private String encryptionKey;

    @Value("${NyanidSetting.HOST}")
    private String HOST;

    @Value("${NyanidSetting.EnableUserRegister}")
    private boolean EnableUserRegister;

    public String EventID = "RegEvent1";

    @Async
    @PostMapping
    public <T> Object RequestPost(@RequestBody(required = false) T data, HttpServletResponse response,HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeyException {
        if (EnableUserRegister) {
            if (data != null){
                JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(data));
                String username = jsonObject.getString("uname");
                String password = jsonObject.getString("pwd");
                String email = jsonObject.getString("e");
                if (username != null | password != null | email != null) {
                    JSONObject Event = new JSONObject();
                    Event.put(EventID,email);
                        if (accountsRepository.findByEmail(email) == null) {
                            if (redisService.getValue(String.valueOf(Event)) == null) {
                                if (!email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}")) {
                                    return ErrRes.IllegalRequestException("邮箱格式错误喵~", response);
                                } else {
                                    if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{8,}$") | password.length() < 7) {
                                        return ErrRes.IllegalRequestException("您的密码至少包含一个大写字母(A-Z),包含一个小写字母(a-z),包含一个数字(0-9),包含一个特殊字符,且长度至少为 8 个字符喵~", response);
                                    } else {
                                        String key = OtherUtils.RandomString(8);
                                        String uid = UUIDHelper.generateNyanIDUUID(key, email).replaceAll("-", "");
                                        String VerificationCode = OtherUtils.RandomString(128);
//                                String scheme = request.getScheme();
//                                String serverName = request.getServerName();
//                                int serverPort = request.getServerPort();
//                                String baseURL = scheme + "://" + serverName + (serverPort != 80 && serverPort != 443 ? ":" + serverPort : "");
                                        // HmacSHA256加密Password
                                        String lockpwd = OtherUtils.HMACSHA256(encryptionKey,password);
                                        emailService.RegisterVerification(email, HOST + "/verification/" + VerificationCode);
                                        JSONObject json = new JSONObject();
                                        json.put("uid", uid);
                                        json.put("email", email);
                                        json.put("username", username);
                                        json.put("password", lockpwd);
                                        redisService.setValueWithExpiration(VerificationCode, json, 300, TimeUnit.SECONDS);
                                        redisService.setValueWithExpiration(String.valueOf(Event), "111", 800, TimeUnit.SECONDS);
                                        SJson sJson = new SJson();
                                        sJson.setMessage("请前往邮箱验证然后完成注册,注意,链接有效期只有5分钟,请尽快验证喵!");
                                        sJson.setStatus(200);
                                        sJson.setTimestamp(LocalDateTime.now());
                                        return sJson;
                                    }
                                }
                            } else {
                                return ErrRes.IllegalRequestException("邮箱已被注册或限制注册喵~", response);
                            }
                        } else {
                            return ErrRes.IllegalRequestException("邮箱已被注册或限制注册喵~", response);
                        }

                } else {
                    return ErrRes.IllegalRequestException("参数错误", response);
                }
            }else return ErrRes.IllegalRequestException("参数错误喵~", response);
        }else {
            return ErrRes.IllegalRequestException("服务器未开启用户注册功能喵~", response);
        }
    }

    @GetMapping
    public Object RequestGet(HttpServletResponse response,HttpServletRequest request) {
        redisService.setValueWithExpiration(request.getLocalAddr(), "1", 3, TimeUnit.SECONDS);
        return ErrRes.IllegalClientException("该接口不支持Get请求喵~",response);
    }
}
