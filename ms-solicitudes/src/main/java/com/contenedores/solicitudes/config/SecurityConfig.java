package com.contenedores.solicitudes.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/health",
                                "/api/solicitudes/health",
                                "/actuator/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Cliente: crea y consulta sus solicitudes
                        .requestMatchers(HttpMethod.POST, "/api/solicitudes/**").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/solicitudes/mias/**").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/solicitudes/historial/**").hasRole("CLIENTE")

                        // Operador: pendientes, contenedores pendientes, finalizar
                        .requestMatchers(HttpMethod.GET, "/api/solicitudes/pendientes").hasRole("OPERADOR")
                        .requestMatchers(HttpMethod.GET, "/api/solicitudes/contenedores-pendientes").hasRole("OPERADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/solicitudes/{id}/finalizar").hasRole("OPERADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/solicitudes/{id}/estado").hasRole("OPERADOR")

                        // Bloqueo por defecto para otros roles
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder(
            @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") String jwkSetUri,
            @Value("${app.security.accepted-issuers:http://localhost:8084/realms/tpi-backend,http://keycloak:8084/realms/tpi-backend}") List<String> acceptedIssuers) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();

        OAuth2TokenValidator<Jwt> timestampValidator = new JwtTimestampValidator();
        OAuth2TokenValidator<Jwt> issuerValidator = token -> {
            String issuer = token.getIssuer() != null ? token.getIssuer().toString() : "";
            if (acceptedIssuers == null || acceptedIssuers.isEmpty() || acceptedIssuers.contains(issuer)) {
                return OAuth2TokenValidatorResult.success();
            }

            return OAuth2TokenValidatorResult.failure(
                    new OAuth2Error(
                            "invalid_token",
                            "Issuer %s no está permitido".formatted(issuer),
                            null
                    )
            );
        };

        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(timestampValidator, issuerValidator));
        return decoder;
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>();

            Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
            if (realmAccess != null && realmAccess.get("roles") instanceof Collection<?> roles) {
                for (Object role : roles) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                }
            }

            JwtGrantedAuthoritiesConverter scopes = new JwtGrantedAuthoritiesConverter();
            authorities.addAll(scopes.convert(jwt));

            return authorities;
        });
        return converter;
    }
}