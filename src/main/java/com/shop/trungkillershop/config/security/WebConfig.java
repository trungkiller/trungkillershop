package com.shop.trungkillershop.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // áp dụng cho tất cả endpoint
                        .allowedOrigins("http://localhost:8080", "http://192.168.49.100") // domain được phép gọi API
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // các method được phép
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // /images/** → ánh xạ đến thư mục uploads/
                registry.addResourceHandler("/images/**")
                        .addResourceLocations("file:uploads/");
            }
        };
    }


}

