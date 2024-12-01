package moe.takanashihoshino.nyaniduserserver.utils.WebMvc;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moe.takanashihoshino.nyaniduserserver.utils.ErrUtils.Error;
import moe.takanashihoshino.nyaniduserserver.utils.ErrUtils.ErrorCode;
import moe.takanashihoshino.nyaniduserserver.utils.RedisUtils.RedisService;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.AccountsRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.NyanIDuserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@Component
public class AuthenticateCheck implements HandlerInterceptor {

    @Autowired
    private NyanIDuserRepository nyanIDuserRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private RedisService redisService;

    private String EventID = "Se1";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String Authorization = request.getHeader("Authorization");
        String getEvent = request.getHeader("Event");
        String session = request.getSession().getId();
        String ip = request.getRemoteAddr();
        String RequestMode = request.getMethod();
        if (Authorization != null && getEvent != null){
            String Token = Authorization.replace("Bearer ", "").replace(" ", "");
            if (nyanIDuserRepository.getAccount(Token) != null){
                String uid = nyanIDuserRepository.getAccount(Token);
                String ClientID =nyanIDuserRepository.getClienID(uid);
                if (!accountsRepository.isBanned(uid)) {
                    switch (getEvent) {
                        case "Ua"://上传头像 PUT
                            if (Objects.equals(RequestMode, "PUT")) {
                                if (!CheckSession(uid)) {
                                    addSession(session, uid, ClientID, ip);
                                    break;
                                }
                                break;
                            } else {
                                PrintWriter(response, Err(ErrorCode.IllegalRequest.getCode(), ErrorCode.IllegalRequest.getMessage(), "Zako~Wrong action event MiaoWu~"), 403);
                                return false;
                            }
                        case "Ud"://上传数据 POST
                            if (Objects.equals(RequestMode, "POST")) {
                                if (!CheckSession(uid)) {
                                    addSession(session, uid, ClientID, ip);
                                    break;
                                }
                                break;
                            } else {
                                PrintWriter(response, Err(ErrorCode.IllegalRequest.getCode(), ErrorCode.IllegalRequest.getMessage(), "Zako~Wrong action event MiaoWu~"), 403);
                                return false;
                            }
                        case "Gi"://获取信息 GET
                            if (Objects.equals(RequestMode, "GET")) {
                                if (!CheckSession(uid)) {
                                    addSession(session, uid, ClientID, ip);
                                    break;
                                }
                                break;
                            } else {
                                PrintWriter(response, Err(ErrorCode.IllegalRequest.getCode(), ErrorCode.IllegalRequest.getMessage(), "Zako~Wrong action event MiaoWu~"), 403);
                                return false;
                            }


                        default:
                            PrintWriter(response, Err(ErrorCode.IllegalRequest.getCode(), ErrorCode.IllegalRequest.getMessage(), "Zako~Unknown action parameters MiaoWu~"), 403);
                            return false;
                    }
                }else {
                    PrintWriter(response,Err(ErrorCode.Unauthorized.getCode(),ErrorCode.Unauthorized.getMessage(),"Zako~account is banned for admin MiaoWu~ "),401);
                    return false;
                }
                     return true;
            }else {
                PrintWriter(response,Err(ErrorCode.Unauthorized.getCode(),ErrorCode.Unauthorized.getMessage(),"Zako~Authentication failed, invalid token MiaoWu~ "),401);
                return false;
            }
        }else {
            PrintWriter(response,Err(ErrorCode.Unauthorized.getCode(),ErrorCode.Unauthorized.getMessage(),"Zako~Authentication failed, invalid token MiaoWu~"), 401);
            return false;
        }
    }


    public boolean CheckSession(String uid){
        JSONObject e = new JSONObject();
        e.put(EventID,uid);
        return redisService.getValue(String.valueOf(e)) != null;
    }

    public void addSession(String session,String uid,String clientID,String ip){
        JSONObject i = new JSONObject();
        i.put(EventID,session);
        i.put("ip",ip);
        if (clientID != null) {
            i.put("clientid", clientID);
        }
        JSONObject e = new JSONObject();
        e.put(EventID,uid);
        redisService.setValueWithExpiration(String.valueOf(e),String.valueOf(i),3,java.util.concurrent.TimeUnit.DAYS);
    }

    public void PrintWriter(HttpServletResponse response,Error error,int code) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setStatus(code);
        out.println(JSONObject.toJSONString(error));
    }
    public Error Err(int CODE ,String MESSAGE,String M) {
        Error error = new Error();
        error.setStatus(CODE);
        error.setError(MESSAGE);
        error.setMessage(M);
        error.setTimestamp(LocalDateTime.now());
        return error;

    }
}
