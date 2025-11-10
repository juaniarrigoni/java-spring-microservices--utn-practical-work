package com.contenedores.apigateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity; // <-- Objeto correcto para WebFlux
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) { // <-- Parámetro correcto
        http
                // La sintaxis para CSRF es ligeramente diferente en WebFlux
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                .authorizeExchange(exchange -> exchange.anyExchange()
                        // Los patrones de ruta se definen directamente en 'pathMatchers'
//                        .pathMatchers(
//                                "/actuator/**",
//                                "/webjars/**",
//                                "/v3/api-docs/**",
//                                "/api/*/swagger-ui.html",
//                                "/api/*/swagger-ui/**)

                                .permitAll()

//                        // Cualquier otra petición requiere autenticación
//                        .anyExchange().authenticated()
                )

                // La sintaxis para configurar JWT también usa Customizer
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}