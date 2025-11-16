package com.contenedores.solicitudes.config;

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
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

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
                                "/api/solicitudes/health",
                                "/actuator/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Solicitudes: reglas por rol
                        .requestMatchers(HttpMethod.POST, "/api/solicitudes").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/solicitudes/pendientes").hasRole("OPERADOR")
                        .requestMatchers(HttpMethod.GET, "/api/solicitudes/contenedor/**").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/solicitudes/contenedores-pendientes").hasRole("OPERADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/solicitudes/*/finalizar").hasRole("OPERADOR")
                        .requestMatchers(HttpMethod.GET, "/api/solicitudes/*/historial").hasAnyRole("CLIENTE", "OPERADOR")
                        .requestMatchers(HttpMethod.GET, "/api/solicitudes/*").hasAnyRole("CLIENTE", "OPERADOR")

                        // Todo lo demás requiere autenticación (evita acceso de transportista/otros)
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