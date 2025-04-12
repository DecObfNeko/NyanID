package moe.takanashihoshino.nyaniduserserver.server.web.User;
//获取用户信息

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Accounts;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.NyanIDuser;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.AccountsRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.NyanIDuserRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.UserDevicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/zako/v1/userinfo")
public class UserInfoApi {

    @Autowired
    private NyanIDuserRepository nyanIDuserRepository;
    @Autowired
    private AccountsRepository accountsRepository;
    @Autowired
    private UserDevicesRepository userDevicesRepository;


    @GetMapping(produces = "application/json")
    public Object GETMethod(HttpServletResponse response, HttpServletRequest request) {
            String Authorization = request.getHeader("Authorization");
            String Token = Authorization.replace("Bearer ", "").replace(" ", "");
            String uid = userDevicesRepository.findUidByToken(Token);
            Accounts accounts = accountsRepository.GetUser(uid);
            NyanIDuser user = nyanIDuserRepository.getUser(uid);
            int exp = user.getExp();
            Boolean isDeveloper =user.isIsDeveloper() ? true : false;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("nickname",user.getNickname());
            jsonObject.put("exp",exp);
            jsonObject.put("description",user.getDescription());
            jsonObject.put("username",accounts.getUsername());
            jsonObject.put("isDeveloper",isDeveloper);
            jsonObject.put("email",accounts.getEmail());
            jsonObject.put("uid",uid);
            if (accounts.getBind() != null){
                String MCUUID = accounts.getBind();
                jsonObject.put("bma",true);
                jsonObject.put("mcuid",MCUUID);
            }
            return jsonObject;






    }
}
