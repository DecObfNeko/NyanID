package moe.takanashihoshino.nyaniduserserver.server.web.Public;


import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moe.takanashihoshino.nyaniduserserver.utils.OtherUtils;
import moe.takanashihoshino.nyaniduserserver.utils.RedisUtils.RedisService;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.AccountsRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.OAuthAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

import static java.lang.Math.random;

@RestController
@RequestMapping("api/zako/v2/server")
public class GetServerInfo {

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private OAuthAppRepository oAuthAppRepository;

    @Autowired
    private RedisService redisService;

    @Value("${NyanidSetting.msg}")
    private String[] msg;

    private  String EventID = "ServerInfo";

    @GetMapping(produces = "application/json")
    public Object GetServerINFO(HttpServletResponse response, HttpServletRequest request) {

        String AllUser = accountsRepository.GetAllUser();
//        String BannedUser = accountsRepository.GetAllBannedUser();
        String GetAllApplication = oAuthAppRepository.GetAllApplication();
        int NumberOfEvents = redisService.getAll();
        JSONObject data = new JSONObject();
        data.put("AllUser",AllUser);
        data.put("msg",msg[(int) (random() * msg.length)]);
//        data.put("BannedUser",BannedUser);
        if(redisService.getValue("ServerInfo") != null){
            JSONObject Notification = JSONObject.parseObject(redisService.getValue("ServerInfo").toString());
            data.put("Notification", true);
            data.put("NotificationType", Notification.get("NotificationType"));
            data.put("NotificationData", Notification.get("NotificationData"));
            data.put("NotificationTypeName",Notification.get("NotificationTypeName"));
        }else {
            data.put("Notification", false);
        }
        data.put("AllApplication",GetAllApplication);
        data.put("NumberOfEvents",NumberOfEvents/6);
        System.out.println(request.getRemoteAddr());
        response.setStatus(200);
        return data;
    }
}
