package moe.takanashihoshino.nyaniduserserver.server.web.Public;


import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletResponse;
import moe.takanashihoshino.nyaniduserserver.utils.RedisUtils.RedisService;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.AccountsRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.OAuthAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/zako/v2/server")
public class GetServerInfo {

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private OAuthAppRepository oAuthAppRepository;

    @Autowired
    private RedisService redisService;

    @GetMapping(produces = "application/json")
    public Object GetServerINFO(HttpServletResponse response) {
        String AllUser = accountsRepository.GetAllUser();
        String BannedUser = accountsRepository.GetAllBannedUser();
        String GetAllApplication = oAuthAppRepository.GetAllApplication();
        int NumberOfEvents = redisService.getAll();
        JSONObject data = new JSONObject();
        data.put("AllUser",AllUser);
        data.put("BannedUser",BannedUser);
        data.put("AllApplication",GetAllApplication);
        data.put("NumberOfEvents",NumberOfEvents/6);
        response.setStatus(200);
        return data;
    }
}
