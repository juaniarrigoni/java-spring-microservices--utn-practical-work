package com.contenedores.apigateway.config;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteConfig.class);

    private final GatewayProperties gatewayProperties;

    public RouteConfig(GatewayProperties gatewayProperties) {
        this.gatewayProperties = gatewayProperties;
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routes = builder.routes();
        if (gatewayProperties.getServices().isEmpty()) {
            LOGGER.warn("No se configuraron rutas para el API Gateway. Verifica la propiedad 'tpi.gateway.services'.");
        }
        for (Map.Entry<String, GatewayProperties.Service> entry : gatewayProperties.getServices().entrySet()) {
            String serviceId = entry.getKey();
            GatewayProperties.Service service = entry.getValue();
            int stripPrefix = Math.max(service.getStripPrefix(), 0);
            routes.route(serviceId, r -> r.path("/api/" + serviceId + "/**")
                    .filters(f -> f.stripPrefix(stripPrefix))
                    .uri(service.getUri()));
            LOGGER.info("Registrada ruta del gateway '{}' hacia {} con stripPrefix {}", serviceId, service.getUri(), stripPrefix);
        }
        return routes.build();
    }
}
