package moe.takanashihoshino.nyaniduserserver.utils.Reload;


import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScopeConfig {
    @Bean
    public static CustomScopeConfigurer customScopeConfigurer(CustomRefreshScope customRefreshScope) {
        CustomScopeConfigurer configurer = new CustomScopeConfigurer();
        configurer.addScope("refresh", customRefreshScope);
        return configurer;
    }
}
