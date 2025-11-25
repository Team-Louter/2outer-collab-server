package com.louter.collab.global.config;

import com.louter.collab.domain.auth.jwt.JwtAuthenticationFilter;
import com.louter.collab.domain.auth.jwt.JwtFilter;
import com.louter.collab.domain.auth.jwt.JwtTokenProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtConfig {

    private String secret;
    private Long expiration;

    @Bean
    @Primary
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(secret, expiration);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(secret, expiration);
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtAuthenticationFilter());
    }
}
