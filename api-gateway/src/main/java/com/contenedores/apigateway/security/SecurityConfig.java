// RUTA: api-gateway/src/main/java/com/contenedores/apigateway/security/SecurityConfig.java
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
                .authorizeExchange(exchange -> exchange
                        // 1. Lista de rutas públicas (Swagger, Health checks)
                        .pathMatchers(
                                "/actuator/**",
                                "/api/*/swagger-ui.html",
                                "/api/*/swagger-ui/**",
                                "/api/*/v3/api-docs"
                        ).permitAll()
                        // 2. Cualquier otra ruta requiere un token JWT válido
                        .anyExchange().authenticated()
                )
                // 3. Habilita la validación de tokens JWT
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}