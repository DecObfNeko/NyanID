package moe.takanashihoshino.nyaniduserserver.server.web.Public;


import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletResponse;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.AccountsRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.NyanIDuserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/zako/v2/userinfo/{uuid}")
public class GetPublicUserInfo {

    @Autowired
    private NyanIDuserRepository nyanIDuserRepository;
    @Autowired
    private AccountsRepository accountsRepository;


    @GetMapping(produces = "application/json")
    public Object GETMethod(@PathVariable String uuid, HttpServletResponse response) {
        String uid = accountsRepository.findByuid(uuid);
        String nickname = nyanIDuserRepository.getNickname(uid);
        int exp = nyanIDuserRepository.getUserEXP(uid);
        Boolean isDeveloper = nyanIDuserRepository.UserIsDeveloper(uid) ? true : false;
        String email = accountsRepository.GetEmailByUid(uid);
        String Description = nyanIDuserRepository.GetDescriptionByUid(uid);
        String Username = accountsRepository.GetUsernameByUid(uid);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nickname",nickname);
        jsonObject.put("exp",exp);
        jsonObject.put("description",Description);
        jsonObject.put("username",Username);
        jsonObject.put("isDeveloper",isDeveloper);
        jsonObject.put("email",email);
        jsonObject.put("uid",uid);
        return jsonObject;






    }
}
