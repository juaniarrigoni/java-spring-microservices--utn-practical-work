package com.contenedores.operaciones.config;

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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

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
                    "/api/operaciones/health",
                    "/actuator/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
            ).permitAll()

            // Gestión de rutas y distancias: solo operador
            .requestMatchers(HttpMethod.GET, "/rutas/**").hasRole("OPERADOR")
            .requestMatchers(HttpMethod.POST, "/rutas/**").hasRole("OPERADOR")
            .requestMatchers("/planificar").hasRole("OPERADOR")
            .requestMatchers("/distancias/**").hasRole("OPERADOR")

            // Asignaciones
            .requestMatchers(HttpMethod.POST, "/asignaciones/**").hasRole("OPERADOR")
            .requestMatchers(HttpMethod.PUT, "/asignaciones/*/confirmar").hasRole("TRANSPORTISTA")
            .requestMatchers(HttpMethod.GET, "/asignaciones/camion/**").hasAnyRole("OPERADOR", "TRANSPORTISTA")

            // Tramos (iniciar/finalizar) y seguimiento: transportista
            .requestMatchers(HttpMethod.PUT, "/tramos/*/iniciar", "/tramos/*/finalizar").hasRole("TRANSPORTISTA")
            .requestMatchers(HttpMethod.POST, "/seguimiento/**").hasRole("TRANSPORTISTA")

            // Cualquier otra ruta requiere autenticación (evita acceso de clientes)
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

            JwtGrantedAuthoritiesConverter scopes = new JwtGrantedAuthoritiesConverter();
            authorities.addAll(scopes.convert(jwt));

            return authorities;
        });
        return converter;
    }
}