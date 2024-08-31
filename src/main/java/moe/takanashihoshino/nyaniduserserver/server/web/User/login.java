package moe.takanashihoshino.nyaniduserserver.server.web.User;
//登录

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
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
                if (accountsRepository.findByEmail(email) != null) {
                    String pwd = accountsRepository.LoginByEmail(email);
                    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
                    SecretKeySpec secret_key = new SecretKeySpec(encryptionKey.getBytes(), "HmacSHA256");
                    sha256_HMAC.init(secret_key);
                    byte[] hash = sha256_HMAC.doFinal(password.getBytes());
                    String lockpwd = Base64.getEncoder().encodeToString(hash);
                    if (Objects.equals(lockpwd, pwd)){
                        String token = OtherUtils.RandomString(128);
                        String cookie = OtherUtils.RandomString(64);
                        String uid = accountsRepository.findByPwd(lockpwd);
                        nyanIDuserRepository.UpdateNyanID(token,cookie,uid);
                        response.setHeader("TOKEN",token);
                        LoginJson loginJson = new LoginJson();
                        loginJson.setData(token);
                        loginJson.setStatus("success");
                        loginJson.setTimestamp(LocalDateTime.now());
                        loginJson.setToken(token);
                        loginJson.setData(Base64.getEncoder().encodeToString(cookie.getBytes())+"."+Base64.getEncoder().encodeToString(token.getBytes()));
                        return loginJson;
                    }else {
                        return ErrRes.NotFoundAccountException("账号不存在喵~",response);
                    }
                }else {
                    return ErrRes.NotFoundAccountException("账号不存在喵~",response);
                }
            }
        }else {
            return ErrRes.IllegalRequestException("参数错误",response);
        }
    }
}
