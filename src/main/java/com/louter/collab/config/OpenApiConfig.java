package com.louter.collab.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        servers = {
                @Server(
                        url = "https://teamcollab.site", // 운영 환경 배포용 URL
                        description = "Production Server"
                ),
                @Server(
                        url = "https://localhost:5173",
                        description = "Local Server"
                )
        }
)
public class OpenApiConfig {
}
