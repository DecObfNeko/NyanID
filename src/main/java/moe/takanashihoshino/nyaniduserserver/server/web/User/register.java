package moe.takanashihoshino.nyaniduserserver.server.web.User;
//注册

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moe.takanashihoshino.nyaniduserserver.ErrUtils.ErrRes;
import moe.takanashihoshino.nyaniduserserver.ErrUtils.SJson;
import moe.takanashihoshino.nyaniduserserver.RedisUtils.RedisService;
import moe.takanashihoshino.nyaniduserserver.SqlUtils.Repository.AccountsRepository;
import moe.takanashihoshino.nyaniduserserver.utils.EmailHelper.EmailService;
import moe.takanashihoshino.nyaniduserserver.utils.OtherUtils;
import moe.takanashihoshino.nyaniduserserver.utils.UUIDHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/zako/v1/reg")
public class register {

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

    @PostMapping
    public Object RequestPost(@RequestBody JSONObject jsonObject, HttpServletResponse response,HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeyException {
            String username = jsonObject.getString("uname");
            String password = jsonObject.getString("pwd");
            String email = jsonObject.getString("e");
            String ip = jsonObject.getString("p");
        if (username != null | password != null | email != null | ip != null){
            if (!Objects.equals(ip, request.getLocalAddr())){
                redisService.setValueWithExpiration(ip, "1", 600, TimeUnit.SECONDS);
                redisService.setValueWithExpiration(request.getLocalAddr(), "1", 600, TimeUnit.SECONDS);
                return ErrRes.Dimples1337Exception("我去你IP怎么对不上喵?",response);
            }else {//
                if (accountsRepository.findByEmail(email) == null) {
                    if (redisService.getValue(email) == null) {
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
                                Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
                                SecretKeySpec secret_key = new SecretKeySpec(encryptionKey.getBytes(), "HmacSHA256");
                                sha256_HMAC.init(secret_key);
                                byte[] hash = sha256_HMAC.doFinal(password.getBytes());
                                String lockpwd = Base64.getEncoder().encodeToString(hash);
                                emailService.RegisterVerification(email, HOST + "/verification/" + VerificationCode);
                                JSONObject json = new JSONObject();
                                json.put("uid", uid);
                                json.put("email", email);
                                json.put("username", username);
                                json.put("password", lockpwd);
                                redisService.setValueWithExpiration(VerificationCode, json, 300, TimeUnit.SECONDS);
                                SJson sJson = new SJson();
                                sJson.setMessage("请前往邮箱验证然后完成注册,注意,链接有效期只有5分钟,请尽快验证喵!");
                                sJson.setStatus(200);
                                sJson.setTimestamp(LocalDateTime.now());
                                redisService.setValueWithExpiration(ip, "1", 4, TimeUnit.SECONDS);
                                redisService.setValueWithExpiration(email, "1", 2, TimeUnit.SECONDS);
                                return sJson;
                            }
                        }
                    }else {
                        return ErrRes.IllegalRequestException("邮箱已被注册或限制注册喵~",response);
                    }
                }else {
                    return ErrRes.IllegalRequestException("邮箱已被注册或限制注册喵~",response);
                }
            }
        }else {
            return ErrRes.IllegalRequestException("参数错误",response);
        }
    }

    @GetMapping
    public Object RequestGet(HttpServletResponse response,HttpServletRequest request) {
        redisService.setValueWithExpiration(request.getLocalAddr(), "1", 3, TimeUnit.SECONDS);
        return ErrRes.IllegalClientException("该接口不支持Get请求喵~",response);
    }
}
