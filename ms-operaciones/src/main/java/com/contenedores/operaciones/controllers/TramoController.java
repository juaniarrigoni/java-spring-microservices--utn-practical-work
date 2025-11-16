package com.contenedores.operaciones.controllers;

import com.contenedores.operaciones.dto.TramoEstadoResponse;
import com.contenedores.operaciones.service.TramoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Keycloak")
@RequestMapping("/tramos")
public class TramoController {
    private final TramoService tramoService;

    public TramoController(TramoService tramoService) {
        this.tramoService = tramoService;
    }

    /**
     * Endpoint para iniciar un tramo de traslado (REQ-7).
     * Método: PUT
     * Ruta: /tramos/{id}/iniciar
     * Valida: Estado PENDIENTE, tiene camión asignado
     * Acción: Cambia estado a EN_CURSO, registra fechaInicioReal
     * Rol: Transportista
     */
    @Operation(summary = "Iniciar un tramo de traslado")
    @PutMapping("/{id}/iniciar")
    public ResponseEntity<TramoEstadoResponse> iniciarTramo(@PathVariable("id") UUID id) {
        TramoEstadoResponse response = tramoService.iniciarTramo(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para finalizar un tramo de traslado (REQ-7).
     * Método: PUT
     * Ruta: /tramos/{id}/finalizar
     * Valida: Estado EN_CURSO
     * Acción: Cambia estado a COMPLETADO, registra fechaFinReal
     * Rol: Transportista
     */
    @Operation(summary = "Finalizar un tramo de traslado")
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<TramoEstadoResponse> finalizarTramo(@PathVariable("id") UUID id) {
        TramoEstadoResponse response = tramoService.finalizarTramo(id);
        return ResponseEntity.ok(response);
    }
}