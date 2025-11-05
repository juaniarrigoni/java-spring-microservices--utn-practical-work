package com.contenedores.operaciones.controllers;

import java.time.Instant;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.contenedores.operaciones.dto.PlanificacionRequest;
import com.contenedores.operaciones.dto.PlanificacionResponse;

@RestController
public class OperacionesController {

    @PostMapping("/planificar")
    public PlanificacionResponse planificar(@RequestBody @Valid PlanificacionRequest request) {
        long duracionEstimada = Math.max(30, 60 + request.paradasIntermedias() * 15L);
        return new PlanificacionResponse(
                request.origen(),
                request.destino(),
                request.paradasIntermedias(),
                "PLANIFICADA",
                Instant.now(),
                duracionEstimada);
    }
}
