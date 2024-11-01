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
    public Object GetMethod(HttpServletResponse response) {
        return ErrRes.IllegalClientException("This API does not support GET requests 杂鱼喵~",response);
    }

    @PostMapping
    public <T> Object PostMethod(@RequestBody T daTa, HttpServletResponse response, HttpServletRequest request) {
        if (daTa != null){
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(daTa));
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
                    sJson.setMessage("The verification is successful, please go to Login 杂鱼喵~");
                    sJson.setTimestamp(LocalDateTime.now());
                    return sJson;
                }else {
                    return ErrRes.IllegalRequestException("The verification code is incorrect or invalid 杂鱼喵~",response);
                }
            }else {
                return ErrRes.IllegalRequestException("The parameter is incorrect 杂鱼喵~",response);
            }
        }else return ErrRes.IllegalClientException("The parameter is incorrect 杂鱼喵~",response);

    }
}
