// RUTA: api-gateway/src/main/java/com/contenedores/apigateway/security/SecurityConfig.java
package com.contenedores.apigateway.security;

import org.springframework.beans.factory.annotation.Value;
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
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                            @Value("${gateway.security.enabled:false}") boolean securityEnabled) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);

        if (!securityEnabled) {
            http.authorizeExchange(exchange -> exchange.anyExchange().permitAll());
            return http.build();
        }
                http.authorizeExchange(exchange -> exchange
                        // Lista de rutas públicas (Swagger, Health checks)
                        .pathMatchers(
                                "/actuator/**",
                                "/api/*/swagger-ui.html",
                                "/api/*/swagger-ui/**",
                                "/api/*/v3/api-docs/**" // Usar /api-docs/** para cubrir sub-rutas
                        ).permitAll()
                        // Cualquier otra ruta requiere un token JWT válido
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}