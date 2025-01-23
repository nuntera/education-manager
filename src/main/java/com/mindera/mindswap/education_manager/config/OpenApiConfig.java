package com.mindera.mindswap.education_manager.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for OpenAPI documentation.
 * This class sets up the Swagger/OpenAPI documentation for the Education Manager API.
 * 
 * The configuration includes:
 * - API title and description
 * - Version information
 * - Base components setup
 * 
 * Access the Swagger UI at: /swagger-ui.html
 * Access the OpenAPI specification at: /v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    /**
     * Creates and configures the OpenAPI documentation bean.
     * 
     * @return Configured OpenAPI instance with application information
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Education Management API")
                        .description("Application for managing students and courses")
                        .version("1.0.0")
                );
    }
}
