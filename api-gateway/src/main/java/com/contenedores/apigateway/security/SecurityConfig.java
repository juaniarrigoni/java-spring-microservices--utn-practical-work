package com.contenedores.apigateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/actuator/**").permitAll() // Permitir acceso a health checks
                        .pathMatchers("/eureka/**").permitAll() // Para un posible Eureka si lo agregamos
                        .anyExchange().authenticated() // Todas las demás deben estar autenticadas
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
                    // Configuración JWT, el issuer-uri se toma de application.yml
                }));
        return http.build();
    }
}