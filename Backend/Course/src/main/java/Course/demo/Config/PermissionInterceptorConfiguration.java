package Course.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PermissionInterceptorConfiguration implements WebMvcConfigurer {
    @Bean
    PermissionInterceptor getPermissionInterceptor() {
        return new PermissionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] whiteList = {
                "/", "/api/auth/**", "/storage/**", "/api/files","/api/payment/**"

        };
        registry.addInterceptor(getPermissionInterceptor())
                .excludePathPatterns(whiteList);
    }
}
