package com.contenedores.apigateway.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

@Configuration
@EnableWebFluxSecurity
@EnableConfigurationProperties(GatewaySecurityProperties.class)
public class SecurityConfig {

    private final GatewaySecurityProperties securityProperties;

    public SecurityConfig(GatewaySecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);

        if (!securityProperties.isEnabled()) {
            http.authorizeExchange(exchange -> exchange.anyExchange().permitAll())
                    .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::disable);
            return http.build();
        }

        http.authorizeExchange(exchange -> exchange
                        // Rutas públicas para Swagger y health checks
                        .pathMatchers(
                                "/actuator/**",
                                "/api/*/swagger-ui.html",
                                "/api/*/webjars/swagger-ui/**",
                                "/api/*/swagger-ui/**",
                                "/api/*/v3/api-docs/**"
                        ).permitAll()
                        // El resto requiere autenticación
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}