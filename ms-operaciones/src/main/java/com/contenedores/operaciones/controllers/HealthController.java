package com.contenedores.operaciones.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping({"/health", "/api/operaciones/health"})
    public Map<String, Object> health() {
        return Map.of(
                "service", "ms-operaciones",
                "status", "UP",
                "version", "0.1.0",
                "timestamp", Instant.now().toString());
    }
}