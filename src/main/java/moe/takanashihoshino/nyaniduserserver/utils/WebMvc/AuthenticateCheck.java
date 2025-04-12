package moe.takanashihoshino.nyaniduserserver.utils.WebMvc;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moe.takanashihoshino.nyaniduserserver.utils.ErrUtils.Error;
import moe.takanashihoshino.nyaniduserserver.utils.ErrUtils.ErrorCode;
import moe.takanashihoshino.nyaniduserserver.utils.RedisUtils.RedisService;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.AccountsRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.BanUserRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.NyanIDuserRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.UserDevicesRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Service.UserDevicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
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
    private UserDevicesService userDevicesService;

    @Autowired
    private BanUserRepository banUserRepository;

    @Autowired
    private UserDevicesRepository userDevicesRepository;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String Authorization = request.getHeader("Authorization");
        String getEvent = request.getHeader("Event");
        String RequestMode = request.getMethod();
        if (Authorization != null && getEvent != null){
            String Token = Authorization.replace("Bearer ", "").replace(" ", "");
            if (userDevicesRepository.findUidByToken(Token) != null){
                String uid = userDevicesRepository.findUidByToken(Token);
                if (banUserRepository.findBanIDByUid(uid) == null) {
                    switch (getEvent) {
                        case "Ua"://上传头像 PUT
                            if (Objects.equals(RequestMode, "PUT")) {
                                break;
                            } else {
                                PrintWriter(response, Err(ErrorCode.IllegalRequest.getCode(), ErrorCode.IllegalRequest.getMessage(), "Zako~Wrong action event MiaoWu~"), 403);
                                return false;
                            }
                        case "Ud"://上传数据 POST
                            if (Objects.equals(RequestMode, "POST")) {

                                break;
                            } else {
                                PrintWriter(response, Err(ErrorCode.IllegalRequest.getCode(), ErrorCode.IllegalRequest.getMessage(), "Zako~Wrong action event MiaoWu~"), 403);
                                return false;
                            }
                        case "Gi"://获取信息 GET
                            if (Objects.equals(RequestMode, "GET")) {
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
