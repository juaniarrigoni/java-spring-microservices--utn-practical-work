package com.contenedores.operaciones.controllers;

import com.contenedores.operaciones.model.AsignacionCamion;
import com.contenedores.operaciones.service.AsignacionCamionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/asignaciones")
@RequiredArgsConstructor
public class AsignacionCamionController {

    private final AsignacionCamionService asignacionService;

    @Operation(summary = "Buscar asignación activa por ID de camión")
    @GetMapping("/camion/{camionId}")
    public ResponseEntity<Optional<AsignacionCamion>> buscarPorCamion(@PathVariable UUID camionId) {
        return ResponseEntity.ok(asignacionService.buscarPorCamion(camionId));
    }
}
