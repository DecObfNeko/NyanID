package moe.takanashihoshino.nyaniduserserver.server.YggdrasilServer;


import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moe.takanashihoshino.nyaniduserserver.server.YggdrasilServer.Authserver.Json.CharacterInformationJson;
import moe.takanashihoshino.nyaniduserserver.server.YggdrasilServer.Authserver.Json.Property;
import moe.takanashihoshino.nyaniduserserver.server.YggdrasilServer.Authserver.Json.TexturesJson;
import moe.takanashihoshino.nyaniduserserver.utils.ErrUtils.ErrRes;
import moe.takanashihoshino.nyaniduserserver.utils.RedisUtils.RedisService;
import moe.takanashihoshino.nyaniduserserver.utils.RsaUtil;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.UserDevicesRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.YggdrasilPlayerRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.YggdrasilRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Yggdrasil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static moe.takanashihoshino.nyaniduserserver.utils.RsaUtil.RSA_SIGNATURE_ALGORITHM;

@RestController
@RequestMapping("api/yggdrasil/sessionserver/session/minecraft")
public class Sessionserver {

    private final YggdrasilRepository yggdrasilRepository;

    private final YggdrasilPlayerRepository yggdrasilPlayerRepository;

    private final UserDevicesRepository userDevicesRepository;

    private final RedisService redisService;

    @Value("${yggdrasil.APILocation}")
    private String APILocation;

    @Value("${yggdrasil.privateKey}")
    private String  privateKey;

    public Sessionserver(YggdrasilRepository yggdrasilRepository, YggdrasilPlayerRepository yggdrasilPlayerRepository, UserDevicesRepository userDevicesRepository, RedisService redisService) {
        this.yggdrasilRepository = yggdrasilRepository;
        this.yggdrasilPlayerRepository = yggdrasilPlayerRepository;
        this.userDevicesRepository = userDevicesRepository;
        this.redisService = redisService;
    }

    @PostMapping("join")
    public <T> Object ClientJoinServerHandle(@RequestBody(required = false) T data,HttpServletResponse response, HttpServletRequest request){
        if (data != null) {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(data));
            if (json.containsKey("accessToken") && json.containsKey("selectedProfile")){
                if (json.containsKey("serverId")) {
                    String accessToken = json.getString("accessToken");
                    String selectedProfile = json.getString("selectedProfile");
                    String serverId = json.getString("serverId");
                    if (!accessToken.isEmpty() && !selectedProfile.isEmpty()) {
                        if (!serverId.isEmpty()){
                            if (userDevicesRepository.findUidByToken(accessToken) != null){
                                    String uid = userDevicesRepository.findUidByToken(accessToken);
                                    if (yggdrasilRepository.GetPlayerUUID(uid)!= null) {
                                        String mcuuid = yggdrasilRepository.GetPlayerUUID(uid);
                                        if (selectedProfile.equals(mcuuid.replace("-",""))){
                                            if (serverId.length() > 8){
                                                JSONObject in = new JSONObject();
                                                in.put("reqIp",request.getRemoteAddr());
                                                in.put("accessToken",accessToken);
                                                redisService.setValueWithExpiration(serverId,JSONObject.toJSONString(in), 30,TimeUnit.SECONDS);
                                                response.setStatus(204);
                                                return null;
                                            }else {
                                                return ErrRes.YggdrasilError("令牌有效,账户核对正确,但似乎这个服务器的serverId并不符合此Yggdrasil服务器的要求杂鱼喵~!", "ForbiddenOperationException", "ForbiddenOperationException", 403, response);
                                            }
                                         }else {
                                            return ErrRes.YggdrasilError("令牌有效,但似乎这个令牌并不属于这个MINECRAFT账户杂鱼喵~!", "ForbiddenOperationException", "ForbiddenOperationException", 403, response);
                                        }
                                    }else {
                                        return ErrRes.YggdrasilError("令牌有效,但似乎您并没有Yggdrasil账户杂鱼喵~!", "ForbiddenOperationException", "ForbiddenOperationException", 403, response);
                                    }
                            }else {
                                return ErrRes.YggdrasilError("令牌无效杂鱼喵~!","ForbiddenOperationException","ForbiddenOperationException",403,response);
                            }
                    }else {
                            return ErrRes.YggdrasilError("你请求的json中缺少重要参数serverId杂鱼喵~!","The parameter is incorrect","The parameter is incorrect 杂鱼喵~",403,response);
                        }
                    }else {
                        return ErrRes.YggdrasilError("你请求的json中缺少重要参数accessToken或selectedProfile杂鱼喵~","The parameter is incorrect","The parameter is incorrect 杂鱼喵~",403,response);
                    }
                }else {
                    return ErrRes.YggdrasilError("你请求的json中缺少重要参数serverId杂鱼喵~!","The parameter is incorrect","The parameter is incorrect 杂鱼喵~",403,response);
                }
            }else {
                return ErrRes.YggdrasilError("你请求的json中缺少重要参数accessToken或selectedProfile杂鱼喵~!","The parameter is incorrect","The parameter is incorrect 杂鱼喵~",403,response);
            }
        }else {
            return ErrRes.YggdrasilError("你请求的内容为NULL杂鱼喵!","The parameter is incorrect","The parameter is incorrect 杂鱼喵~",403,response);
        }
    }
    @GetMapping("hasJoined")
    public Object ServerVerifyClient(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String username = request.getParameter("username");
        String serverId = request.getParameter("serverId");
        String ip = request.getParameter("ip");
        if (username != null && serverId != null){
            if (redisService.getValue(serverId) != null){
                JSONObject out = JSONObject.parseObject(redisService.getValue(serverId).toString());
                redisService.deleteValue(serverId);
                String accessToken = out.getString("accessToken");
                if (userDevicesRepository.findUidByToken(accessToken) != null){
                String nuid = userDevicesRepository.findUidByToken(accessToken);
                String MCuid = yggdrasilRepository.GetPlayerUUID(nuid);
                String mcname = yggdrasilRepository.GetPlayerNAME(nuid);
                if (username.equals(mcname)){
                    Yggdrasil yggdrasil = yggdrasilRepository.YggdrasilPlayer(MCuid);
                    String MODEL;
                    // 创建 SKIN 纹理
                    if (yggdrasilPlayerRepository.getSkinTexturesType(MCuid) == 1){
                        MODEL =  "default";
                    }else {
                        MODEL =  "slim";
                    }
                    JSONObject TexturesJson = new JSONObject();
                    TexturesJson.put("timestamp",System.currentTimeMillis());
                    TexturesJson.put("profileId",MCuid.replace("-",""));
                    TexturesJson.put("profileName",mcname);
                    TexturesJson.put("signatureRequired",true);
                    TexturesJson.putObject("textures");
                    JSONObject textures = TexturesJson.putObject("textures");
                    // 2. 处理 SKIN
                    if (yggdrasil.getUseSkin()) {
                        moe.takanashihoshino.nyaniduserserver.server.YggdrasilServer.Authserver.Json.TexturesJson.SkinTexture s = new TexturesJson.SkinTexture(APILocation + "/api/zako/res/textures/" + yggdrasilPlayerRepository.getSkinTexturesHash(yggdrasil.getUuid()), new TexturesJson.TextureMetadata(MODEL));

                        // 直接放入统一的 textures 对象
                        textures.put("SKIN", s);
                    }
                    // 3. 处理 CAPE
                    if (yggdrasil.getUseCAPE()) {
                        moe.takanashihoshino.nyaniduserserver.server.YggdrasilServer.Authserver.Json.TexturesJson.SkinTexture d = new TexturesJson.SkinTexture(APILocation + "/api/zako/res/textures/" + yggdrasilPlayerRepository.getCAPETexturesHash(yggdrasil.getUuid()), null);

                        // 直接放入统一的 textures 对象
                        textures.put("CAPE", d);
                    }
                    String   Sign = RsaUtil.sign(Base64.getEncoder().encodeToString(TexturesJson.toString().getBytes()).getBytes(), privateKey,RSA_SIGNATURE_ALGORITHM  );

                    // 1. 创建示例数据
                    List<Property> properties = new ArrayList<>();
                    properties.add(new Property("textures", Base64.getEncoder().encodeToString(TexturesJson.toString().getBytes()), Sign));

                    return new CharacterInformationJson(
                            yggdrasil.getUuid().replace("-",""),
                            yggdrasil.getPlayername(),
                            properties
                    );


                 }else {
                    return ErrRes.YggdrasilError("无效的会话杂鱼喵!","ForbiddenOperationException","ForbiddenOperationException",403,response);
                }
                }else {
                    return ErrRes.YggdrasilError("无效的会话杂鱼喵!","ForbiddenOperationException","ForbiddenOperationException",403,response);
                }
            }else {
                return ErrRes.YggdrasilError("无效的会话杂鱼喵!","ForbiddenOperationException","ForbiddenOperationException",403,response);
            }
        }else {
            return ErrRes.YggdrasilError("你请求的内容为NULL杂鱼喵!","The parameter is incorrect","The parameter is incorrect 杂鱼喵~",403,response);
        }
    }
    @GetMapping({"profile/{uuid}","profile/*","profile"})
    public Object GetPlayer(@PathVariable String uuid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String unsignedParam = request.getParameter("unsigned");
        boolean Unsigned;
        String Sign;
        if (unsignedParam != null){
            Unsigned = Boolean.parseBoolean(unsignedParam);
        }else {
            Unsigned = true;
        }
        if (uuid != null ) {
            String UUID = uuid.replace("-", "");
            if (UUID.length() == 32){
                String fulluuid = java.util.UUID.fromString(UUID
                        .replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{12})",
                                "$1-$2-$3-$4-$5")
                ).toString();
                if (yggdrasilRepository.YggdrasilPlayer(fulluuid) != null){
                    Yggdrasil yggdrasil = yggdrasilRepository.YggdrasilPlayer(fulluuid);
                    String MODEL;
                    // 创建 SKIN 纹理
                    if (yggdrasilPlayerRepository.getSkinTexturesType(yggdrasil.getUuid()) == 1){
                        MODEL =  "default";
                    }else {
                        MODEL =  "slim";
                    }
                    JSONObject TexturesJson = new JSONObject();
                    TexturesJson.put("timestamp",System.currentTimeMillis());
                    TexturesJson.put("profileId",yggdrasil.getUuid().replace("-",""));
                    TexturesJson.put("profileName",yggdrasil.getPlayername());
                    TexturesJson.put("signatureRequired", !Unsigned);
                    TexturesJson.putObject("textures");
                    JSONObject textures = TexturesJson.putObject("textures");
                    // 2. 处理 SKIN
                    if (yggdrasil.getUseSkin()) {
                        moe.takanashihoshino.nyaniduserserver.server.YggdrasilServer.Authserver.Json.TexturesJson.SkinTexture s = new TexturesJson.SkinTexture(APILocation + "/api/zako/res/textures/" + yggdrasilPlayerRepository.getSkinTexturesHash(yggdrasil.getUuid()), new TexturesJson.TextureMetadata(MODEL));

                        // 直接放入统一的 textures 对象
                        textures.put("SKIN", s);
                    }
                    // 3. 处理 CAPE
                    if (yggdrasil.getUseCAPE()) {
                        moe.takanashihoshino.nyaniduserserver.server.YggdrasilServer.Authserver.Json.TexturesJson.SkinTexture d = new TexturesJson.SkinTexture(APILocation + "/api/zako/res/textures/" + yggdrasilPlayerRepository.getCAPETexturesHash(yggdrasil.getUuid()), null);

                        // 直接放入统一的 textures 对象
                        textures.put("CAPE", d);
                    }
                    if (!Unsigned){
                        Sign = RsaUtil.sign(Base64.getEncoder().encodeToString(TexturesJson.toString().getBytes()).getBytes(), privateKey,RSA_SIGNATURE_ALGORITHM  );
                    }else {
                        Sign = null;
                    }
                    // 1. 创建示例数据
                    List<Property> properties = new ArrayList<>();
                    properties.add(new Property("textures", Base64.getEncoder().encodeToString(TexturesJson.toString().getBytes()), Sign));

                    return new CharacterInformationJson(
                            yggdrasil.getUuid().replace("-",""),
                            yggdrasil.getPlayername(),
                            properties
                    );

                   }else {
                    return ErrRes.YggdrasilError("玩家不存在杂鱼喵!","Not Found","Not Found",404,response);
                }
            }else {
                return ErrRes.YggdrasilError("非法UUID杂鱼喵!","The parameter is incorrect","The parameter is incorrect 杂鱼喵~",403,response);
            }
        }else {
            return ErrRes.YggdrasilError("你请求的内容为NULL杂鱼喵!","The parameter is incorrect","The parameter is incorrect 杂鱼喵~",403,response);
        }
    }
}
