package ru.itq.ordermanagement.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Это класс конфигуратор для Swagger
 */
@Configuration
public class openApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Order Management API")
                        .version("1.0.0")
                        .description("API для управления заказами"))
                .externalDocs(new ExternalDocumentation()
                        .description("Документация API")
                        .url("/v3/api-docs.yaml"));
    }
}
