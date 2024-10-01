package moe.takanashihoshino.nyaniduserserver.server.web.User;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moe.takanashihoshino.nyaniduserserver.ErrUtils.ErrRes;
import moe.takanashihoshino.nyaniduserserver.ErrUtils.SJson;
import moe.takanashihoshino.nyaniduserserver.RedisUtils.RedisService;
import moe.takanashihoshino.nyaniduserserver.SqlUtils.Repository.AccountsRepository;
import moe.takanashihoshino.nyaniduserserver.SqlUtils.Repository.NyanIDuserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;



//邮箱验证
@RestController
@RequestMapping("api/zako/v1/verification")
public class AuthenticateEmailApi {

    @Autowired
    private NyanIDuserRepository nyanIDuserRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private RedisService redisService;


    @GetMapping
    public Object verifyCode(HttpServletResponse response) {
        return ErrRes.IllegalClientException("该接口不支持Get请求喵~",response);
    }

    @PostMapping
    public Object PostMethod(@RequestBody JSONObject jsonObject, HttpServletResponse response, HttpServletRequest request) {
        String code = jsonObject.getString("code");
        if (code != null | request.getHeader("AuthenticateEmail") != null){
            if (redisService.getValue(code) != null){
                JSONObject data = JSONObject.parseObject(redisService.getValue(code).toString());
                String uid = data.getString("uid");
                String password =data.getString("password");
                String username =data.getString("username");
                String email =data.getString("email");
                accountsRepository.CreateNewAccount(uid,email,password,username);
                nyanIDuserRepository.CreateNyanID(uid,"还没想好取啥名字的新猫猫");
                redisService.deleteValue(code);
                SJson sJson = new SJson();
                sJson.setStatus(200);
                sJson.setMessage("验证成功,请前往登录喵~");
                sJson.setTimestamp(LocalDateTime.now());
                return sJson;
            }else {
                return ErrRes.IllegalRequestException("验证码错误或失效",response);
            }
        }else {
            return ErrRes.IllegalRequestException("参数错误",response);
        }
    }


}
