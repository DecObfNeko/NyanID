package moe.takanashihoshino.nyaniduserserver.server.web.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moe.takanashihoshino.nyaniduserserver.utils.ErrUtils.ErrRes;
import moe.takanashihoshino.nyaniduserserver.utils.RedisUtils.RedisService;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Service.NyanidUserService;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Service.UserService;
import org.springframework.web.bind.annotation.*;


//邮箱验证
@RestController
@RequestMapping("api/zako/v1/verification")
public class AuthenticateEmailAPI {


    private final RedisService redisService;


    private final UserService userService;


    private final NyanidUserService nyanidUserService;

    public AuthenticateEmailAPI(RedisService redisService, UserService userService, NyanidUserService nyanidUserService) {
        this.redisService = redisService;
        this.userService = userService;
        this.nyanidUserService = nyanidUserService;
    }


    @GetMapping
    public Object GetMethod(HttpServletResponse response) {
        return ErrRes.IllegalClientException("This API does not support GET requests 杂鱼喵~",response);
    }

    @PostMapping
    public Object PostMethod(HttpServletResponse response, HttpServletRequest request) {
        String code = request.getHeader("code");
        if (code != null) {
            return ErrRes.IllegalRequestException("The parameter is incorrect 杂鱼喵~", response);









        } else {
            return ErrRes.IllegalRequestException("The parameter is incorrect 杂鱼喵~", response);
        }
    }
    }
