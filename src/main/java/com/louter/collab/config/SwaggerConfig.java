package com.louter.collab.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Security 설정 (JWT)
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION);

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Authorization");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Authorization", securityScheme))
                .addSecurityItem(securityRequirement)
                .info(new Info()
                        .title("Collab API")
                        .description("Collab 프로젝트의 REST API 문서입니다.")
                        .version("1.0.0"));
    }
}
