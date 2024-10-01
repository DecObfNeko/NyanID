package moe.takanashihoshino.nyaniduserserver.server.web.User;
//用户数据修改

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moe.takanashihoshino.nyaniduserserver.ErrUtils.ErrRes;
import moe.takanashihoshino.nyaniduserserver.ErrUtils.Error;
import moe.takanashihoshino.nyaniduserserver.ErrUtils.ErrorCode;
import moe.takanashihoshino.nyaniduserserver.ErrUtils.SJson;
import moe.takanashihoshino.nyaniduserserver.SqlUtils.Repository.NyanIDuserRepository;
import moe.takanashihoshino.nyaniduserserver.utils.OtherUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@RestController
@RequestMapping("api/zako/v1/userdata")
public class UserDataApi {
    @PostMapping
    public Object PostMethod(){
        return "asdPostMethod";
    }

    @Autowired
    private NyanIDuserRepository nyanIDuserRepository;

    @PutMapping(produces = "application/json")
    public <T> Object PutMethod(@RequestParam(value = "avatar", required = false) T avatar, HttpServletRequest request , HttpServletResponse response) throws IOException {
        if (avatar  != null ){
            MultipartFile avatarFile = (MultipartFile) avatar;
            if (!request.getContentType().matches("multipart/form-data") &&avatarFile.getContentType() != null){
                String ContentType = avatarFile.getContentType();
                if (ContentType.matches("image/.*")) {
                    String Authorization = request.getHeader("Authorization");
                    String Token = Authorization.replace("Bearer ", "").replace(" ", "");
                    String uid = nyanIDuserRepository.getAccount(Token);
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
            OtherUtils.reduceImageByRatio(avatar.getInputStream(), UserAvatar,uid,10, 10);
        }else {
            System.out.println(false);
            OtherUtils.reduceImageByRatio(avatar.getInputStream(), UserAvatar,uid,10, 10);
            //originalFile.delete();
        }












        String newavatarMD5 = DigestUtils.md5DigestAsHex(avatar.getBytes());
        System.out.println(newavatarMD5);

//        InputStream a = avatar.getInputStream();
//        BufferedImage imag = ImageIO.read(a);
//        a.close();




//        avatar.getSize();
//        InputStream is = avatar.getInputStream();
//        BufferedImage imag = ImageIO.read(is);
//        System.out.println(imag.getWidth());
//        System.out.println(imag.getHeight());
//        System.out.println(imag.getColorModel().getPixelSize());
//        System.out.println(imag.getColorModel().getTransferType());
//        System.out.println(imag.getColorModel().getTransparency());
//        System.out.println(Arrays.toString(imag.getColorModel().getComponentSize()));
//        System.out.println(imag.getColorModel().getNumColorComponents());
//        ImageIO.write(imag, "png", new File(UserAvatar + uid + ".png"));
//        is.close();
    }


    }
