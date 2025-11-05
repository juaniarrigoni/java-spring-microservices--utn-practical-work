package com.contenedores.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("catalogos", r -> r.path("/api/catalogos/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("http://ms-catalogos:8081"))
                .route("solicitudes", r -> r.path("/api/solicitudes/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("http://ms-solicitudes:8082"))
                .route("operaciones", r -> r.path("/api/operaciones/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("http://ms-operaciones:8083"))
                .build();
    }
}
