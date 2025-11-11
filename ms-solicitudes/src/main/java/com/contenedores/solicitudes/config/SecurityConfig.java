package com.contenedores.solicitudes.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> {
                    // 1. Endpoints Públicos (sin autenticación)
                    authorize.requestMatchers(
                            "/actuator/**",
                            "/swagger-ui.html",
                            "/swagger-ui/**",
                            "/v3/api-docs/**"
                    ).permitAll();

                    // 2. Reglas para Solicitudes
                    // Un CLIENTE puede crear una nueva solicitud.
                    authorize.requestMatchers(HttpMethod.POST, "/solicitudes").hasRole("CLIENTE");

                    // Un OPERADOR puede ver TODAS las solicitudes.
                    authorize.requestMatchers(HttpMethod.GET, "/solicitudes").hasRole("OPERADOR");

                    // Un CLIENTE o un OPERADOR pueden ver UNA solicitud específica por su ID.
                    authorize.requestMatchers(HttpMethod.GET, "/solicitudes/{id}").hasAnyRole("CLIENTE", "OPERADOR");

                    // Un OPERADOR actualiza el estado de la solicitud (validar, planificar, etc.).
                    // Un CLIENTE podría actualizarla para cancelarla, por ejemplo.
                    authorize.requestMatchers(HttpMethod.PUT, "/solicitudes/{id}").hasAnyRole("OPERADOR", "CLIENTE");

                    // Solo un OPERADOR puede eliminar una solicitud (acción destructiva).
                    authorize.requestMatchers(HttpMethod.DELETE, "/solicitudes/{id}").hasRole("OPERADOR");

                    // 3. Regla Final: Cualquier otra petición debe ser autenticada
                    authorize.anyRequest().authenticated();
                })
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
                );

        return http.build();
    }
}