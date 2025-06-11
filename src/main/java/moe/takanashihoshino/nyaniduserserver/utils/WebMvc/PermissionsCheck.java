package moe.takanashihoshino.nyaniduserserver.utils.WebMvc;


import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerInterceptor;

@RestController
@Component
public class PermissionsCheck implements HandlerInterceptor {
}
