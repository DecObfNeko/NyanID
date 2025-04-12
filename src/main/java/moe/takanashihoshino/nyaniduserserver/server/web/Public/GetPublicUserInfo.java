package moe.takanashihoshino.nyaniduserserver.server.web.Public;


import com.alibaba.fastjson2.JSONObject;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Accounts;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.NyanIDuser;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.AccountsRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.BanUserRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.NyanIDuserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        Accounts accounts = accountsRepository.GetUser(uuid);
        NyanIDuser user = nyanIDuserRepository.getUser(accounts.getUid());
        int exp = user.getExp();
        Boolean isDeveloper = user.isIsDeveloper() ? true : false;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nickname",user.getNickname());
        jsonObject.put("exp",exp);
        jsonObject.put("description",user.getDescription());
        jsonObject.put("username",accounts.getUsername());
        jsonObject.put("isDeveloper",isDeveloper);
        jsonObject.put("uid",accounts.getUid());
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
