package br.com.evandro.todoList.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI().info( new Info().title("Gerenciamento de tarefas")
                .description("Sistema/API responsável por gerenciar tarefas diárias")
                .version("1"))
                .schemaRequirement("jwt_auth", securityScheme());
    }

    private SecurityScheme securityScheme(){
        return new SecurityScheme().name("jwt_auth")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER);
    }

}
