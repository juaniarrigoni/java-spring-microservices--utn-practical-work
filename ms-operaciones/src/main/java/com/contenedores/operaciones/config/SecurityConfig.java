package com.contenedores.operaciones.config;

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
                    // 1. Endpoints Públicos
                    authorize.requestMatchers(
                            "/actuator/**",
                            "/swagger-ui.html",
                            "/swagger-ui/**",
                            "/v3/api-docs/**"
                    ).permitAll();

                    // 2. Reglas para Rutas: Solo el OPERADOR puede planificar y gestionar rutas.
                    authorize.requestMatchers("/rutas/**").hasRole("OPERADOR");

                    // 3. Reglas para Tramos: El TRANSPORTISTA ejecuta los tramos.
                    authorize.requestMatchers(HttpMethod.PUT, "/tramos/{id}/iniciar").hasRole("TRANSPORTISTA");
                    authorize.requestMatchers(HttpMethod.PUT, "/tramos/{id}/finalizar").hasRole("TRANSPORTISTA");

                    // Un OPERADOR podría necesitar ver los detalles de un tramo.
                    authorize.requestMatchers(HttpMethod.GET, "/tramos/**").hasAnyRole("OPERADOR", "TRANSPORTISTA");

                    // 4. Reglas para Asignaciones
                    // Un OPERADOR crea la asignación (esto se haría a través de la lógica de rutas/tramos).
                    // El TRANSPORTISTA confirma la asignación que se le ha dado.
                    authorize.requestMatchers(HttpMethod.PUT, "/asignaciones/{id}/confirmar").hasRole("TRANSPORTISTA");
                    // Ambos roles pueden necesitar ver las asignaciones de un camión.
                    authorize.requestMatchers(HttpMethod.GET, "/asignaciones/camion/{camionId}").hasAnyRole("OPERADOR", "TRANSPORTISTA");

                    // 5. Reglas para Seguimiento: El TRANSPORTISTA reporta su estado.
                    authorize.requestMatchers("/seguimiento/**").hasRole("TRANSPORTISTA");

                    // 6. Regla Final: Cualquier otra petición debe ser autenticada
                    authorize.anyRequest().authenticated();
                })
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
                );

        return http.build();
    }
}