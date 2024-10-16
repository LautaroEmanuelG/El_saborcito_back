// src/main/java/utn/saborcito/El_saborcito_back/config/OpenApiConfig.java
package utn.saborcito.El_saborcito_back.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API El Saborcito")
                        .description("API para el sistema de El Saborcito"));
    }
}