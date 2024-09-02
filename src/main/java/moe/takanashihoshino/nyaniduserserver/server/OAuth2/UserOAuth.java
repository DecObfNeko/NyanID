package moe.takanashihoshino.nyaniduserserver.server.OAuth2;


import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("oauth2/api/auth")
public class UserOAuth {

    @PostMapping
    public Object PostMethod(@RequestBody JSONObject data, HttpServletResponse response, HttpServletRequest request) {
        return null;
    }
}
