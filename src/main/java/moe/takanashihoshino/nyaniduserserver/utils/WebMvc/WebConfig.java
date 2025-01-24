package moe.takanashihoshino.nyaniduserserver.utils.WebMvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 可限制哪个请求可以通过跨域
                .allowedHeaders("*")  // 可限制固定请求头可以通过跨域
                .allowedMethods("*") // 可限制固定methods可以通过跨域
                .allowedOrigins("*")  // 可限制访问ip可以通过跨域
                .allowCredentials(true) // 是否允许发送cookie
                .exposedHeaders(HttpHeaders.SET_COOKIE);
    }

}