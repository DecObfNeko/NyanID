package moe.takanashihoshino.nyaniduserserver.server.web.User;
//忘记密码


import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moe.takanashihoshino.nyaniduserserver.ErrUtils.ErrRes;
import moe.takanashihoshino.nyaniduserserver.ErrUtils.SJson;
import moe.takanashihoshino.nyaniduserserver.RedisUtils.RedisService;
import moe.takanashihoshino.nyaniduserserver.SqlUtils.Repository.AccountsRepository;
import moe.takanashihoshino.nyaniduserserver.SqlUtils.Repository.NyanIDuserRepository;
import moe.takanashihoshino.nyaniduserserver.utils.EmailHelper.EmailService;
import moe.takanashihoshino.nyaniduserserver.utils.OtherUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("api/zako/v1/forgetpwd")
public class ForgetPwdApi {


@Autowired
public NyanIDuserRepository nyanIDuserRepository;
@Autowired
public AccountsRepository accountsRepository;
@Autowired
public EmailService emailService;
@Autowired
public RedisService redisService;
public String EventID = "FP1";

@Value("${NyanidSetting.encryptionKey}")
private String encryptionKey;


@PostMapping(produces = "application/json")
public <T> Object VerifyCode(@RequestBody(required = false) T data, HttpServletResponse response, HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeyException {
        if (data != null){
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(data));
            String email = jsonObject.getString("email");
            String code = jsonObject.getString("code");
            String newpwd = jsonObject.getString("password");
            String ip = request.getRemoteAddr();
            if (email != null && code != null){
                if (email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}")){
                    if (redisService.getValue(code) != null){
                        redisService.deleteValue(code);
                        String pwd = OtherUtils.HMACSHA256(encryptionKey,newpwd);
                        String uid = accountsRepository.findByEmail(email);
                        accountsRepository.UpdatePassword(email,pwd);
                        emailService.NotificationEmail(email,ip,"修改密码",uid);
                        SJson sJson = new SJson();
                        sJson.setMessage("密码修改成功");
                        sJson.setStatus(200);
                        sJson.setTimestamp(LocalDateTime.now());
                        return sJson;
                    }else return ErrRes.IllegalClientException("验证码错误或已过期", response);
                }else return ErrRes.IllegalRequestException("邮箱格式错误", response);
            }else return ErrRes.IllegalRequestException("参数错误", response);
        }else return ErrRes.IllegalRequestException("参数错误", response);
    }
@GetMapping
public Object GetAuthenticateEmail(@RequestParam(value = "email",required = false) String email, HttpServletResponse response){
    if (email != null){
        if (email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(EventID, email);
            if (accountsRepository.findByEmail(email) != null) {
                if (redisService.getValue(String.valueOf(jsonObject)) != null){
                    return ErrRes.IllegalClientException("请勿重复发送验证码", response);
                }else {
                    String code = OtherUtils.RandomString(8);
                    redisService.setValueWithExpiration(code, email, 300, java.util.concurrent.TimeUnit.SECONDS);
                    emailService.sendVerificationCode(email, code);
                    SJson sJson = new SJson();
                    sJson.setMessage("验证码已发送至邮箱");
                    sJson.setStatus(200);
                    sJson.setTimestamp(LocalDateTime.now());
                    redisService.setValueWithExpiration(String.valueOf(jsonObject), "1", 360, java.util.concurrent.TimeUnit.SECONDS);
                    return sJson;
                }
            } else return ErrRes.NotFoundAccountException("账号不存在或尝试找回次数过多", response);
        }else return ErrRes.IllegalRequestException("邮箱格式错误", response);
    }else return ErrRes.IllegalRequestException("参数错误", response);
}



}
