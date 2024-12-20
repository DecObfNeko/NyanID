package moe.takanashihoshino.nyaniduserserver.utils.WebMvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private IPSecurityDetection ipSecurityDetection;

    @Autowired
    private AuthenticateCheck authenticateCheck;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ipSecurityDetection).addPathPatterns("/authserver/**").addPathPatterns("/api/zako/v1/login").addPathPatterns("api/zako/v1/register");
        registry.addInterceptor(authenticateCheck).addPathPatterns("/api/zako/v1/userdata").addPathPatterns("/api/zako/v1/userinfo");
    }
}