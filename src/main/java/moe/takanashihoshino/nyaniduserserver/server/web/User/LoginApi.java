package moe.takanashihoshino.nyaniduserserver.server.web.User;


import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moe.takanashihoshino.nyaniduserserver.utils.ErrUtils.ErrRes;
import moe.takanashihoshino.nyaniduserserver.utils.RedisUtils.RedisService;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.AccountsRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.BanUserRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.NyanIDuserRepository;
import moe.takanashihoshino.nyaniduserserver.server.web.User.UserJson.LoginJson;
import moe.takanashihoshino.nyaniduserserver.utils.OtherUtils;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.UserDevicesRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Service.UserDevicesService;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.UserDevices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/zako/v1/login")
public class LoginApi {

    @Autowired
    private RedisService redisService;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private UserDevicesService userDevicesService;

    @Autowired
    private UserDevicesRepository userDevicesRepository;

    @Autowired
    private BanUserRepository banUserRepository;

    @Value("${NyanidSetting.encryptionKey}")
    private String encryptionKey;
    private final Map<String, LoginApi.Const> constMap = new HashMap<>();

    public  String EventID = "LoEvent1";
    @PostMapping()
    public <T> Object PostMethod(@RequestBody(required = false) T data, HttpServletResponse response, HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeyException {
        if (data != null){
            JSONObject a = JSONObject.parseObject(JSONObject.toJSONString(data));
            String email = a.getString("email");
            String password = a.getString("pwd");
            String DevicesID = a.getString("devid");
            Boolean LoginForWeb = Boolean.valueOf(request.getHeader("LoginForWeb"));
            String DevicesName = a.getString("devname");
            String IP = request.getRemoteAddr();
            if (email != null && password != null){
                JSONObject BanEvent = new JSONObject();
                BanEvent.put(EventID, email);
                    if (!email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}")) {
                        return ErrRes.IllegalRequestException("The mailbox is malformed 杂鱼喵~", response);
                    } else {
                        if (accountsRepository.findByEmail(email) != null) {
                            if (redisService.getValue(String.valueOf(BanEvent)) != null) {
                                return ErrRes.NotFoundAccountException("The account doesn't exist or is locked because of a password error 杂鱼喵~", response);
                            } else {
                                if (constMap.get(email) == null) {
                                    constMap.put(email, new Const(1));
                                } else if (constMap.get(email).requestCount > 3) {
                                    constMap.remove(email);
                                    redisService.setValueWithExpiration(String.valueOf(BanEvent), "1", 180, TimeUnit.SECONDS);
                                }
                                if (LoginForWeb){
                                    String DevicesIDT = OtherUtils.RandomString(128);
                                    String DevicesNameT = "Web";
                                    DevicesName  = DevicesNameT;
                                    DevicesID = DevicesIDT;
                                }else {
                                    if(DevicesID==null || DevicesName==null) {
                                        return ErrRes.IllegalRequestException("The device ID or device name is missing 杂鱼喵~", response);
                                    }
                                }
                                    String pwd = accountsRepository.LoginByEmail(email);
                                    String lockpwd = OtherUtils.HMACSHA256(encryptionKey,password);
                                    if (Objects.equals(lockpwd, pwd)) {
                                        String uid = accountsRepository.findByEmail(email);
                                        String session = request.getSession().getId();
                                        if (banUserRepository.findBanIDByUid(uid) == null) {
                                            if (Objects.equals(userDevicesRepository.findSessionBySession(session), session) || !LoginForWeb) {
                                                System.out.print(session);
                                                String token = userDevicesRepository.findTokenBySession(session);
                                                String clientid = userDevicesRepository.findClientIdByToken(token);
                                                response.setHeader("TOKEN", token);
                                                LoginJson loginJson = new LoginJson();
                                                loginJson.setData(clientid);
                                                loginJson.setStatus("success");
                                                loginJson.setTimestamp(LocalDateTime.now());
                                                loginJson.setToken(token);
                                                loginJson.setData(Base64.getEncoder().encodeToString(token.getBytes()));
                                                return loginJson;
                                            } else {
                                                String clientid = OtherUtils.RandomString(128);
                                                String token = OtherUtils.RandomString(64);
                                                UserDevices userDevices = new UserDevices();
                                                userDevices.setUid(uid);
                                                userDevices.setDeviceID(DevicesID);
                                                userDevices.setDeviceName(DevicesName);
                                                userDevices.setToken(token);
                                                userDevices.setIsActive(true);
                                                userDevices.setSession(DevicesID);
                                                userDevices.setClientid(clientid);
                                                userDevices.setExpireTime(LocalDateTime.now().plusDays(7));
                                                userDevicesService.save(userDevices);
                                                if (constMap.get(email) != null) {
                                                    constMap.remove(email);
                                                }
                                                response.setHeader("TOKEN", token);
                                                LoginJson loginJson = new LoginJson();
                                                loginJson.setData(clientid);
                                                loginJson.setStatus("success");
                                                loginJson.setTimestamp(LocalDateTime.now());
                                                loginJson.setToken(token);
                                                loginJson.setData(Base64.getEncoder().encodeToString(token.getBytes()));
                                                return loginJson;
                                            }
                                        } else {
                                            return ErrRes.NotFoundAccountException("This account has been banned for violating our User Agreement, please create a ticket to appeal 杂鱼喵~ ", response);
                                    }
                                } else {
                                    if (constMap.get(email) != null) {
                                        constMap.get(email).requestCount++;
                                    }
                                    return ErrRes.NotFoundAccountException("The account doesn't exist or is locked because of a password error 杂鱼喵~", response);
                                }
                            }
                        } else {
                            return ErrRes.NotFoundAccountException("The account doesn't exist or is locked because of a password error 杂鱼喵~", response);
                        }
                    }
            }else {
                return ErrRes.IllegalRequestException("The parameter is incorrect 杂鱼喵~",response);
            }

        }else return ErrRes.IllegalRequestException("The parameter is incorrect 杂鱼喵~",response);
    }



    @GetMapping
    public Object GetMethod(HttpServletResponse response){
        return ErrRes.MethodNotAllowedException("Unsupported request patterns 杂鱼喵~",response);
    }


    private static class Const {
        int requestCount;
        Const(int requestCount) {
            this.requestCount = requestCount;
        }
    }
}
