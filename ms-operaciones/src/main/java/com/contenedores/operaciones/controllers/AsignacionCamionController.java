package com.contenedores.operaciones.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.RestController;

import com.contenedores.operaciones.dto.AsignacionCamionRequest;
import com.contenedores.operaciones.dto.AsignacionCamionResponse;
import com.contenedores.operaciones.model.AsignacionCamion;
import com.contenedores.operaciones.service.AsignacionCamionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Keycloak")
@RequestMapping("/asignaciones") // Ruta más limpia sin el prefijo /api
@RequiredArgsConstructor
public class AsignacionCamionController {

    private final AsignacionCamionService asignacionService;

    @Operation(summary = "Buscar todas las asignaciones para un camión por su ID",
               description = "Obtiene la lista de todas las asignaciones de tramos para un camión específico")
    @GetMapping("/camion/{camionId}")
    public ResponseEntity<List<AsignacionCamion>> buscarPorCamion(
            @Parameter(description = "UUID del camión", required = true, 
                       schema = @Schema(type = "string", format = "uuid", 
                                       example = "b8f1b9c5-1c22-4b89-9a75-0193f1a0e111"))
            @PathVariable("camionId") UUID camionId) {
        // Devuelve una lista, ya que un camión puede tener múltiples asignaciones
        List<AsignacionCamion> asignaciones = asignacionService.buscarPorCamion(camionId);
        return ResponseEntity.ok(asignaciones);
    }

    @Operation(summary = "Confirmar una asignación de tramo por parte del transportista",
               description = "Confirma que el transportista acepta la asignación del tramo")
    @PutMapping("/{asignacionId}/confirmar")
    public ResponseEntity<AsignacionCamion> confirmarAsignacion(
            @Parameter(description = "UUID de la asignación", required = true,
                       schema = @Schema(type = "string", format = "uuid",
                                       example = "c9f2c9d6-2d33-5c90-0b86-1204f2b1f222"))
            @PathVariable("asignacionId") UUID asignacionId) {
        AsignacionCamion asignacionConfirmada = asignacionService.confirmarAsignacion(asignacionId);
        return ResponseEntity.ok(asignacionConfirmada);
    }

    /**
     * Endpoint para asignar un camión a un tramo de traslado (REQ-6).
     * Método: POST
     * Ruta: /asignaciones
     * Body: { "tramoId": "uuid", "camionId": "uuid" }
     * Rol: Operador / Administrador
     */
    @Operation(summary = "Asignar camión a un tramo de traslado")
    @PostMapping
    public ResponseEntity<AsignacionCamionResponse> asignarCamionATramo(@RequestBody AsignacionCamionRequest request) {
        AsignacionCamionResponse response = asignacionService.asignarCamionATramo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}