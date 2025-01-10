package com.creditas.simuladorcredito.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Simulador de Crédito API", version = "1.0", description = "API para simular condições de empréstimos"))
public class SwaggerConfig {
}