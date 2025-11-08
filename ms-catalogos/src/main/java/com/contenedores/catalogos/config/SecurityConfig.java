package com.contenedores.catalogos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitar CSRF porque usamos una API REST stateless
                .csrf(AbstractHttpConfigurer::disable)

                // Definir las reglas de autorización
                .authorizeHttpRequests(authorize -> authorize
                        // Permitir el acceso público a todos los endpoints de Actuator
                        .requestMatchers("/actuator/**").permitAll()

                        // Requerir autenticación para cualquier otra petición
                        .anyRequest().authenticated()
                )

                // Configurar el servidor para que valide tokens JWT (esto lo usaremos más adelante)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}