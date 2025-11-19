package com.louter.collab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                                // HTTP localhost
                                "http://localhost:5500",
                                "http://localhost:3000",
                                "http://localhost:5173",
                                "http://localhost:8080",
                                // HTTPS localhost
                                "https://localhost:5500",
                                "https://localhost:3000",
                                "https://localhost:5173",
                                "https://localhost:8080",
                                // url
                                "https://api.teamcollab.site",
                                "http://api.teamcollab.site"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("Origin", "X-Requested-With", "Content-Type", "Accept", "Authorization")
                        .allowCredentials(true);
            }
        };
    }
}
