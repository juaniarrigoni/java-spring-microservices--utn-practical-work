package com.contenedores.catalogos.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitar CSRF porque usamos una API REST stateless
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/health",
                                "/api/catalogos/health",
                                "/actuator/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Solo Operador puede listar/crear/editar entidades de catálogo
                        .requestMatchers(
                                HttpMethod.GET,
                                "/camiones/**",
                                "/depositos/**",
                                "/tarifas/**",
                                "/tarifas-volumen/**",
                                "/configuracion-tarifas/**",
                                "/version"
                        ).hasRole("OPERADOR")
                        .requestMatchers(
                                HttpMethod.POST,
                                "/camiones/**",
                                "/depositos/**",
                                "/tarifas/**",
                                "/tarifas-volumen/**",
                                "/configuracion-tarifas/**"
                        ).hasRole("OPERADOR")
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/camiones/**",
                                "/depositos/**",
                                "/tarifas/**",
                                "/tarifas-volumen/**",
                                "/configuracion-tarifas/**"
                        ).hasRole("OPERADOR")
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/tarifas-volumen/**",
                                "/configuracion-tarifas/**"
                        ).hasRole("OPERADOR")
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/camiones/**",
                                "/depositos/**",
                                "/tarifas/**",
                                "/tarifas-volumen/**",
                                "/configuracion-tarifas/**"
                        ).hasRole("OPERADOR")

                        // Cualquier otro endpoint requiere autenticación (sin acceso para transportista/cliente)
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
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

            // Retain scope based authorities if present
            JwtGrantedAuthoritiesConverter scopes = new JwtGrantedAuthoritiesConverter();
            authorities.addAll(scopes.convert(jwt));

            return authorities;
        });
        return converter;
    }
}