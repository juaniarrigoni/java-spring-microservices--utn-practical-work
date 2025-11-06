package com.contenedores.operaciones.controllers;

import com.contenedores.operaciones.dto.AsignarCamionRequest;
import com.contenedores.operaciones.model.EstadoTramo;
import com.contenedores.operaciones.model.Tramo;
import com.contenedores.operaciones.service.TramoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tramos")
@RequiredArgsConstructor
public class TramoController {

    private final TramoService tramoService;

    @Operation(summary = "Listar tramos por ID de ruta")
    @GetMapping("/ruta/{rutaId}")
    public ResponseEntity<List<Tramo>> listarPorRuta(@PathVariable UUID rutaId) {
        return ResponseEntity.ok(tramoService.listarPorRuta(rutaId));
    }

    @Operation(summary = "Obtener un tramo por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Tramo> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(tramoService.obtenerPorId(id));
    }

    @Operation(summary = "Cambiar el estado de un tramo")
    @PutMapping("/{id}/estado")
    public ResponseEntity<Tramo> cambiarEstado(
            @PathVariable UUID id,
            @RequestParam EstadoTramo nuevoEstado) {
        return ResponseEntity.ok(tramoService.cambiarEstado(id, nuevoEstado));
    }

    @Operation(summary = "Asignar un camión a un tramo (vía path)")
    @PostMapping("/{id}/asignar/{camionId}")
    public ResponseEntity<String> asignarCamionPath(
            @PathVariable UUID id,
            @PathVariable UUID camionId) {
        tramoService.asignarCamion(id, camionId);
        return ResponseEntity.ok("Camión asignado correctamente");
    }

    @Operation(summary = "Asignar un camión a un tramo (vía body DTO)")
    @PostMapping("/asignar")
    public ResponseEntity<String> asignarCamionBody(@RequestBody AsignarCamionRequest dto) {
        tramoService.asignarCamion(dto.tramoId(), dto.camionId());
        return ResponseEntity.ok("Camión asignado correctamente");
    }
}
