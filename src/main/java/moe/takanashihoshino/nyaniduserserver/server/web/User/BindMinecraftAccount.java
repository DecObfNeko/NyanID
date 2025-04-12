package moe.takanashihoshino.nyaniduserserver.server.web.User;


import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moe.takanashihoshino.nyaniduserserver.utils.ErrUtils.ErrRes;
import moe.takanashihoshino.nyaniduserserver.utils.ErrUtils.Error;
import moe.takanashihoshino.nyaniduserserver.utils.ErrUtils.SJson;
import moe.takanashihoshino.nyaniduserserver.utils.RedisUtils.RedisService;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Accounts;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.AccountsRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.NyanIDuserRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.UserDevicesRepository;
import moe.takanashihoshino.nyaniduserserver.websocket.server.BungeeConnectHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/zako/v1/bma")
public class BindMinecraftAccount {

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private UserDevicesRepository userDevicesRepository;

    @Autowired
    private RedisService redisService;

    @Autowired
    private BungeeConnectHandle bungeeConnectHandle;

    @PostMapping(produces = "application/json")
    public <T> Object PostMethod(@RequestBody(required = false) T data, HttpServletResponse response, HttpServletRequest request) {
        String Authorization = request.getHeader("Authorization");
        String Token = Authorization.replace("Bearer ", "").replace(" ", "");
        String uid = userDevicesRepository.findUidByToken(Token);
        bungeeConnectHandle.sendMessage(uid);
        if (data != null){
            try {
                JSONObject d = JSONObject.parseObject(JSONObject.toJSONString(data));
                String code = d.getString("code");
                Object UUID = redisService.getValue(code);
                if (UUID != null){
                    accountsRepository.BindMinecraftAccount(UUID.toString(), uid);
                    Accounts accounts = accountsRepository.find(UUID.toString());
                    ResBind resBind = new ResBind();
                    resBind.setPacket("S01");
                    resBind.setUuid(UUID.toString());
                    resBind.setNuid(accounts.getUid());
                    SJson s = new SJson();
                    s.setStatus(200);
                    s.setMessage("绑定成功喵~,uuid: "+UUID);
                    s.setTimestamp(LocalDateTime.now());
                    bungeeConnectHandle.sendMessage(JSONObject.toJSONString(resBind));
                    redisService.deleteValue(code);
                    return s;
                }else return ErrRes.Dimples1337Exception("无效的绑定码杂鱼喵~",response);
            }catch (Exception e){
                return  ErrRes.IllegalRequestException("Illegal Request",response);
            }
        }else return ErrRes.IllegalRequestException("Illegal Request",response);
    }
}
