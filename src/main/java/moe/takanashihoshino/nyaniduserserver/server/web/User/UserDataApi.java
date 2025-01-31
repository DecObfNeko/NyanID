package moe.takanashihoshino.nyaniduserserver.server.web.User;
//用户数据修改

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moe.takanashihoshino.nyaniduserserver.utils.ErrUtils.ErrRes;
import moe.takanashihoshino.nyaniduserserver.utils.ErrUtils.Error;
import moe.takanashihoshino.nyaniduserserver.utils.ErrUtils.ErrorCode;
import moe.takanashihoshino.nyaniduserserver.utils.ErrUtils.SJson;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.AccountsRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.NyanIDuserRepository;
import moe.takanashihoshino.nyaniduserserver.utils.EmailHelper.EmailService;
import moe.takanashihoshino.nyaniduserserver.utils.OtherUtils;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.UserDevicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequestMapping("api/zako/v1/userdata")
public class UserDataApi {

    @Autowired
    private NyanIDuserRepository nyanIDuserRepository;

    @Autowired
    private UserDevicesRepository userDevicesRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private EmailService emailService;


    @PostMapping(produces = "application/json")
    public <T> Object PostMethod(@RequestBody(required = false) T data, HttpServletResponse response, HttpServletRequest request){
        String Authorization = request.getHeader("Authorization");
        String Token = Authorization.replace("Bearer ", "").replace(" ", "");
        String uid = userDevicesRepository.findUidByToken(Token);
        if (data != null){
           JSONObject a = JSONObject.parseObject(JSONObject.toJSONString(data));
           String Action = a.getString("action");
        switch (Action){
                        case "0" :{//设置昵称
                            String NICKNAME = a.getString("nickname");
                            if (NICKNAME != null && NICKNAME.length() > 2){
                                if (!Objects.equals(nyanIDuserRepository.getNickname(uid), NICKNAME)){
                                    nyanIDuserRepository.UpdateNickname(NICKNAME,uid);
                                    response.setStatus(200);
                                    return success("Setting nickname success MiaoWu~",200);
                                }else return ErrRes.IllegalRequestException("The nickname is the same as in the server  MiaoWu~",response);
                            }else return ErrRes.IllegalRequestException("nickname is invalid  MiaoWu~",response);
                        }
                        case "1" :{//更改用户名
                            String username = a.getString("username");
                            if (username != null && username.length() > 5 && username.matches(".*[A-Za-z0-9]")) {
                                if (accountsRepository.findByUsername(username) == null){
                                    accountsRepository.UpdateUsername(username,uid);
                                    emailService.NotificationEmail(accountsRepository.GetEmailByUid(uid),request.getRemoteAddr(),"Change username",uid);
                                    return success("Setting username success MiaoWu~",200);
                                }else return ErrRes.IllegalRequestException("username is already exist  MiaoWu~",response);
                            }else return ErrRes.IllegalRequestException("username is invalid  MiaoWu~",response);
                        }

            default:
                return ErrRes.IllegalRequestException("RequestBody action is invalid  MiaoWu~",response);
         }
        }else return ErrRes.IllegalRequestException("RequestBody  is NULL  MiaoWu~",response);
    }


    @PutMapping(produces = "application/json")
    public <T> Object PutMethod(@RequestParam(value = "avatar", required = false) T avatar, HttpServletRequest request , HttpServletResponse response) throws IOException {
        if (avatar  != null ){
            MultipartFile avatarFile = (MultipartFile) avatar;
            if (!request.getContentType().matches("multipart/form-data") &&avatarFile.getContentType() != null){
                String ContentType = avatarFile.getContentType();
                if (ContentType.matches("image/.*") && avatarFile.getSize() < 1024 * 1024 * 10) {
                    String Authorization = request.getHeader("Authorization");
                    String Token = Authorization.replace("Bearer ", "").replace(" ", "");
                    String uid = userDevicesRepository.findUidByToken(Token);
                    SaveUserAvatar(uid, avatarFile);
                    SJson sJson = new SJson();
                    sJson.setMessage("Setting avatar success MiaoWu~");
                    sJson.setStatus(200);
                    sJson.setTimestamp(LocalDateTime.now());
                    return JSONObject.toJSONString(sJson);
                }else return ErrRes.IllegalRequestException("RequestParam avatar is not image MiaoWu~",response);
            }else {
                return ErrRes.IllegalRequestException("RequestParam avatar is NULL  MiaoWu~",response);
            }
        }else return ErrRes.IllegalRequestException("RequestParam avatar is NULL  MiaoWu~",response);
    }

    @RequestMapping(produces = "application/json")
    public Object OtherMethod(HttpServletResponse response){
        Error error = new Error();
        error.setError(ErrorCode.MethodNotAllowed.getMessage());
        error.setMessage("Wrong action event MiaoWu~");
        error.setStatus(ErrorCode.MethodNotAllowed.getCode());
        error.setTimestamp(LocalDateTime.now());
        response.setStatus(405);
        return JSONObject.toJSONString(error);
    }

    public void SaveUserAvatar(String uid, MultipartFile avatar) throws IOException {
        Path UserAvatar = Paths.get("Data/UserAvatar/UA-");
        File originalFile = new File(UserAvatar+uid+".png");
        if (originalFile.isFile()) {
            originalFile.delete();
            OtherUtils.reduceImageByRatio(avatar.getInputStream(), UserAvatar,uid,1, 1);
        }else {
            //System.out.println(false);
            OtherUtils.reduceImageByRatio(avatar.getInputStream(), UserAvatar,uid,1, 1);
        }
//        String newavatarMD5 = DigestUtils.md5DigestAsHex(avatar.getBytes());
//        System.out.println(newavatarMD5);
    }

    public static SJson success(String message,int code){
        SJson sJson = new SJson();
        sJson.setMessage(message);
        sJson.setStatus(code);
        sJson.setTimestamp(LocalDateTime.now());
        return sJson;
    }


    }
