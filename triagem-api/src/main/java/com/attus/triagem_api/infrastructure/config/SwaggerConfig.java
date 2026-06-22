package com.attus.triagem_api.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Triagem de Processos - ATTUS")
                        .version("1.0.0")
                        .description("Sistema de triagem e entranhamento automatizado de processos judiciais seguindo o padrão CNJ.")
                        .contact(new Contact()
                                .name("Suporte Técnico")
                                .email("suporte@attus.com.br")));
    }
}