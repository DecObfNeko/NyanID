package moe.takanashihoshino.nyaniduserserver.utils.WebMvc;


import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moe.takanashihoshino.nyaniduserserver.ErrUtils.Error;
import moe.takanashihoshino.nyaniduserserver.ErrUtils.ErrorCode;
import moe.takanashihoshino.nyaniduserserver.RedisUtils.RedisService;
import moe.takanashihoshino.nyaniduserserver.utils.OtherUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Component
public class IPSecurityDetection implements HandlerInterceptor {
    private final Map<String,AccessTime> accessMap = new HashMap<>();

    @Value("${NyanidSetting.MAX_REQUESTS_PER_SECOND}")
    private  long MAX_REQUESTS_PER_SECOND;

    @Value("${NyanidSetting.TIME_FRAME_IN_MILLISECONDS}")
    private long TIME_FRAME_IN_MILLISECONDS; // 毫秒

    @Autowired
    private RedisService redisService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String ip = getIpAddress(request);
        long currentTime = System.currentTimeMillis();
        AccessTime accessTime = accessMap.get(ip);
        if (accessTime == null || currentTime - accessTime.lastAccessTime > TIME_FRAME_IN_MILLISECONDS) {
            accessMap.put(ip, new AccessTime(currentTime, 0));
        } else {
            if (accessTime.requestCount >= MAX_REQUESTS_PER_SECOND) {
                redisService.setValueWithExpiration(ip, 1, 5, TimeUnit.SECONDS);
                accessMap.remove(ip);
                } else {
                    accessTime.requestCount++;
                }
        }
        if (FindIP(ip)){
            if (OtherUtils.RandomIntNumberW() >= 50){
                accessMap.remove(ip);
            }
            PrintWriter out = response.getWriter();
            Error error = new Error();
            error.setStatus(ErrorCode.Dimples1337.getCode());
            error.setError(ErrorCode.Dimples1337.getMessage());
            error.setMessage("Zako~Your IP has been blocked MiaoWu~");
            error.setTimestamp(LocalDateTime.now());
            response.setContentType("application/json");
            response.setStatus(999);
            out.println(JSONObject.toJSONString(error));
            return false;
        }else {
            return true;
        }

    }

    public Boolean FindIP(String ip) {
        if (redisService.getValue(ip) != null){
            return true;
        } else {
            return false;
        }
    }


    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private static class AccessTime {
        long lastAccessTime;
        int requestCount;

        AccessTime(long lastAccessTime, int requestCount) {
            this.lastAccessTime = lastAccessTime;
            this.requestCount = requestCount;
        }
    }

}
