package com.contenedores.operaciones.controllers;

import com.contenedores.operaciones.model.AsignacionCamion;
import com.contenedores.operaciones.service.AsignacionCamionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/asignaciones") // Ruta más limpia sin el prefijo /api
@RequiredArgsConstructor
public class AsignacionCamionController {

    private final AsignacionCamionService asignacionService;

    @Operation(summary = "Buscar todas las asignaciones para un camión por su ID")
    @GetMapping("/camion/{camionId}")
    public ResponseEntity<List<AsignacionCamion>> buscarPorCamion(@PathVariable UUID camionId) {
        // Devuelve una lista, ya que un camión puede tener múltiples asignaciones
        List<AsignacionCamion> asignaciones = asignacionService.buscarPorCamion(camionId);
        return ResponseEntity.ok(asignaciones);
    }

    @Operation(summary = "Confirmar una asignación de tramo por parte del transportista")
    @PutMapping("/{asignacionId}/confirmar")
    public ResponseEntity<AsignacionCamion> confirmarAsignacion(@PathVariable UUID asignacionId) {
        AsignacionCamion asignacionConfirmada = asignacionService.confirmarAsignacion(asignacionId);
        return ResponseEntity.ok(asignacionConfirmada);
    }
}