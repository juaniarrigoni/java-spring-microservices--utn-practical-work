package com.contenedores.catalogos.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.time.Instant;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "Keycloak")
public class HealthController {

    @GetMapping({"/health", "/api/catalogos/health"})
    public Map<String, Object> health() {
        return Map.of(
                "service", "ms-catalogos",
                "status", "UP",
                "version", "0.1.0",
                "timestamp", Instant.now().toString());
    }
}
