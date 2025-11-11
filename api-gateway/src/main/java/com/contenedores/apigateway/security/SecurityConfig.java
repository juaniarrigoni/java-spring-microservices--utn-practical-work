package com.contenedores.apigateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange.anyExchange().permitAll());
//                        // 1. Lista de rutas públicas (la "lista de invitados")
//                        .pathMatchers(
//                                // Endpoints de salud
//                                "/actuator/**",
//                                // Archivos estáticos de Swagger UI
//                                "/webjars/**",
//                                // La definición OpenAPI JSON de cada microservicio
//                                "/v3/api-docs/**",
//                                // La página principal de Swagger para CUALQUIER microservicio
//                                "     ",
//                                // Otros recursos necesarios para la UI de Swagger
//                                "/api/*/swagger-ui/**"
//                        ).permitAll()
//
//                        // 2. Cualquier otra ruta requiere autenticación
//                        .anyExchange().authenticated()
//                )
//                // 3. Habilita la validación de tokens JWT
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}