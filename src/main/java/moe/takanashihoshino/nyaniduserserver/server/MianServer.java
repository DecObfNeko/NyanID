package moe.takanashihoshino.nyaniduserserver.server;

import jakarta.servlet.http.HttpServletResponse;
import moe.takanashihoshino.nyaniduserserver.ErrUtils.SJson;
import moe.takanashihoshino.nyaniduserserver.utils.OtherUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/")
public class MianServer {

    @PostMapping
    public Object PostMethod(HttpServletResponse response){
        response.setHeader("NekoServer-ABYDOS-NYANID", OtherUtils.RandomString(10));
        SJson sJson = new SJson();
        sJson.setStatus(200);
        sJson.setMessage("Ok!");
        sJson.setTimestamp(LocalDateTime.now());
        return  sJson;
    }
    @GetMapping
    public Object GetMethod(HttpServletResponse response){
        response.setHeader("NekoServer-ABYDOS-NYANID", OtherUtils.RandomString(10));
        SJson sJson = new SJson();
        sJson.setStatus(200);
        sJson.setMessage("Ok!");
        sJson.setTimestamp(LocalDateTime.now());
        return  sJson;
    }
}
