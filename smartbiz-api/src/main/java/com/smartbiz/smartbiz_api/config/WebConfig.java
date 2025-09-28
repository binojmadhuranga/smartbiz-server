package com.smartbiz.smartbiz_api.config;

import com.smartbiz.smartbiz_api.interceptor.JwtAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtAuthInterceptor jwtAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthInterceptor)
                .addPathPatterns(
                        "/api/customers/**",
                        "/api/items/**",
                        "/api/suppliers/**",
                        "/api/employees/**",
                        "/api/sales/**",
                        "/api/dashboard/**",
                        "/api/account/**",
                        "/api/users/**",
                        "/api/reports/**",
                        "/api/ai/**",
                        "/api/posts/**"
                )
                .excludePathPatterns(
                        "/api/auth/**",
                        "/api/public/**"
                );
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173") // frontend origin
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
