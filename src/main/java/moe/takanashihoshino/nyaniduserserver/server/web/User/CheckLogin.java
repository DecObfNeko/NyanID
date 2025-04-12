package moe.takanashihoshino.nyaniduserserver.server.web.User;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/zako/v1/cl")
public class CheckLogin {
    @GetMapping(produces = "application/json")
    public String Get(HttpServletResponse response) {
        response.setStatus(204);
        return null;
    }
}
