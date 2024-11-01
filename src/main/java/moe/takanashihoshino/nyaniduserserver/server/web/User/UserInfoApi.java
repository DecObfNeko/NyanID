package moe.takanashihoshino.nyaniduserserver.server.web.User;
//获取用户信息

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moe.takanashihoshino.nyaniduserserver.SqlUtils.Repository.NyanIDuserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/zako/v1/userinfo")
public class UserInfoApi {

    @Autowired
    private NyanIDuserRepository nyanIDuserRepository;


    @GetMapping(produces = "application/json")
    public Object GETMethod(HttpServletResponse response, HttpServletRequest request) {
            String Authorization = request.getHeader("Authorization");
            String Token = Authorization.replace("Bearer ", "").replace(" ", "");
            String uid = nyanIDuserRepository.getAccount(Token);
            String username = nyanIDuserRepository.getNickname(uid);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username",username);
            return jsonObject;






    }
}
