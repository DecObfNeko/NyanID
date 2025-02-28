package moe.takanashihoshino.nyaniduserserver.server.web.Public;


import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletResponse;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.AccountsRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.BanUserRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.NyanIDuserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
    @Autowired
    private BanUserRepository banUserRepository;


    @Async
    @GetMapping(produces = "application/json")
    public Object GETMethod(@PathVariable String uuid) {
        if (banUserRepository.findBanIDByUid(uuid) == null ) {
        String uid = accountsRepository.findByuid(uuid);
        String nickname = nyanIDuserRepository.getNickname(uid);
        int exp = nyanIDuserRepository.getUserEXP(uid);
        Boolean isDeveloper = nyanIDuserRepository.UserIsDeveloper(uid) ? true : false;
        String Description = nyanIDuserRepository.GetDescriptionByUid(uid);
        String Username = accountsRepository.GetUsernameByUid(uid);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nickname",nickname);
        jsonObject.put("exp",exp);
        jsonObject.put("description",Description);
        jsonObject.put("username",Username);
        jsonObject.put("isDeveloper",isDeveloper);
        jsonObject.put("uid",uid);
        return jsonObject;
    }else {
            String banID = banUserRepository.findBanIDByUid(uuid);
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("nickname","该用户已被禁封");
            jsonObject1.put("exp",-1000);
            jsonObject1.put("description","该用户因为违反我们的服务条款或账号准则已被冻结, BanID: " +banID);
            jsonObject1.put("username","BANNED");
            jsonObject1.put("isDeveloper",false);
            jsonObject1.put("uid",uuid);
            return jsonObject1;

        }
    }
}
