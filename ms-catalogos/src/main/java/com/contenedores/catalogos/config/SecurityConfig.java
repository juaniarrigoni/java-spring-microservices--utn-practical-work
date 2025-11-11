package com.contenedores.catalogos.config; // <-- ¡Asegúrate de que el paquete sea el correcto!

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
                    // 1. Endpoints Públicos (rutas locales del microservicio)
                    authorize.requestMatchers(
                            "/actuator/**",
                            "/swagger-ui.html",
                            "/swagger-ui/**",
                            "/v3/api-docs/**"
                    ).permitAll();

                    // 2. Reglas para Camiones
                    authorize.requestMatchers(HttpMethod.POST, "/camiones").hasRole("OPERADOR");
                    authorize.requestMatchers(HttpMethod.GET, "/camiones").authenticated();

                    // 3. Reglas para Depósitos
                    authorize.requestMatchers(HttpMethod.POST, "/depositos").hasRole("OPERADOR");
                    authorize.requestMatchers(HttpMethod.GET, "/depositos").authenticated();

                    // 4. Reglas para Tarifas
                    authorize.requestMatchers(HttpMethod.POST, "/tarifas").hasRole("OPERADOR");
                    authorize.requestMatchers(HttpMethod.GET, "/tarifas").authenticated();

                    // 5. Regla por Defecto
                    authorize.anyRequest().authenticated();
                })
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
                );

        return http.build();
    }
}