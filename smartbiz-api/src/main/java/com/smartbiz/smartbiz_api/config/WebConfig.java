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
                .addPathPatterns("/api/customers/**", "/api/items/**", "/api/suppliers/**", "/api/employees/**", "/api/customers/**" , "/api/sales/**" ,"/api/dashboard/**", "/api/account/**")
                .excludePathPatterns("/api/auth/**", "/api/public/**"); // adjust to your public endpoints
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // allow all /api paths
                .allowedOrigins("http://localhost:5173") // frontend origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }


}