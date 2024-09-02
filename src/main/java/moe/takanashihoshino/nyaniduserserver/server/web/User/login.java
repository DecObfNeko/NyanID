package moe.takanashihoshino.nyaniduserserver.server.web.User;


import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moe.takanashihoshino.nyaniduserserver.ErrUtils.ErrRes;
import moe.takanashihoshino.nyaniduserserver.RedisUtils.RedisService;
import moe.takanashihoshino.nyaniduserserver.SqlUtils.Repository.AccountsRepository;
import moe.takanashihoshino.nyaniduserserver.SqlUtils.Repository.NyanIDuserRepository;
import moe.takanashihoshino.nyaniduserserver.server.web.User.UserJson.LoginJson;
import moe.takanashihoshino.nyaniduserserver.utils.OtherUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/zako/v1/login")
public class login {

    @Autowired
    private RedisService redisService;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private NyanIDuserRepository nyanIDuserRepository;

    @Value("${NyanidSetting.encryptionKey}")
    private String encryptionKey;
    private final Map<String, login.Const> constMap = new HashMap<>();
    @PostMapping
    public Object PostMethod(@RequestBody JSONObject data, HttpServletResponse response, HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeyException {
        String email = data.getString("email");
        String password = data.getString("pwd");
        String ip = data.getString("p");
        if (email != null | password != null | ip != null){
            if (!Objects.equals(ip, request.getLocalAddr())){
                redisService.setValueWithExpiration(ip, "1", 600, TimeUnit.SECONDS);
                redisService.setValueWithExpiration(request.getLocalAddr(), "1", 600, TimeUnit.SECONDS);
                return ErrRes.Dimples1337Exception("我去你IP怎么对不上喵?",response);
            }else {
                if (!email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}")) {
                    return ErrRes.IllegalRequestException("邮箱格式错误喵~", response);
                } else {
                    if (accountsRepository.findByEmail(email) != null) {
                        if (redisService.getValue(email) != null) {
                            return ErrRes.NotFoundAccountException("账号不存在或因为密码错误次数过多锁定喵~", response);
                        } else {
                            if (constMap.get(email) == null) {
                                constMap.put(email, new Const(1));
                            } else if (constMap.get(email).requestCount > 3) {
                                constMap.remove(email);
                                redisService.setValueWithExpiration(email, "1", 180, TimeUnit.SECONDS);
                            }
                            String pwd = accountsRepository.LoginByEmail(email);
                            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
                            SecretKeySpec secret_key = new SecretKeySpec(encryptionKey.getBytes(), "HmacSHA256");
                            sha256_HMAC.init(secret_key);
                            byte[] hash = sha256_HMAC.doFinal(password.getBytes());
                            String lockpwd = Base64.getEncoder().encodeToString(hash);
                            if (Objects.equals(lockpwd, pwd)) {
                                String token = OtherUtils.RandomString(128);
                                String cookie = OtherUtils.RandomString(64);
                                String uid = accountsRepository.findByPwd(lockpwd);
                                nyanIDuserRepository.UpdateNyanID(token, cookie, uid);
                                if (constMap.get(email) != null) {
                                    constMap.remove(email);
                                }
                                response.setHeader("TOKEN", token);
                                LoginJson loginJson = new LoginJson();
                                loginJson.setData(token);
                                loginJson.setStatus("success");
                                loginJson.setTimestamp(LocalDateTime.now());
                                loginJson.setToken(token);
                                loginJson.setData(Base64.getEncoder().encodeToString(cookie.getBytes()) + "." + Base64.getEncoder().encodeToString(token.getBytes()));
                                return loginJson;
                            } else {
                                if (constMap.get(email) != null) {
                                    constMap.get(email).requestCount++;
                                }
                                return ErrRes.NotFoundAccountException("账号不存在或因为密码错误次数过多锁定喵~", response);
                            }
                        }
                    } else {
                        return ErrRes.NotFoundAccountException("账号不存在或因为密码错误次数过多锁定喵~", response);
                    }
                }
            }
        }else {
            return ErrRes.IllegalRequestException("参数错误",response);
        }
    }



    @GetMapping
    public Object GetMethod(HttpServletResponse response){
        return ErrRes.MethodNotAllowedException("不支持的请求模式",response);
    }


    private static class Const {
        int requestCount;
        Const(int requestCount) {
            this.requestCount = requestCount;
        }
    }
}
