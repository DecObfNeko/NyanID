package moe.takanashihoshino.nyaniduserserver.server;

import jakarta.servlet.http.HttpServletResponse;
import moe.takanashihoshino.nyaniduserserver.ErrUtils.ErrRes;
import moe.takanashihoshino.nyaniduserserver.utils.UUIDHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MianServer {

    @PostMapping
    public Object PostMethod(HttpServletResponse response){
        return  ErrRes.IllegalClientException(UUIDHelper.generateUUID("sdasd","sdasd"),response);
    }
    @GetMapping
    public Object GetMethod(HttpServletResponse response){
        return  ErrRes.IllegalClientException(UUIDHelper.generateUUID("sdasd","sdasd"),response);
    }
}
