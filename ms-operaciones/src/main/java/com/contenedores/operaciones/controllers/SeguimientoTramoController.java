package com.contenedores.operaciones.controllers;

import com.contenedores.operaciones.dto.SeguimientoTramoRequest;
import com.contenedores.operaciones.model.SeguimientoTramo;
import com.contenedores.operaciones.model.Tramo;
import com.contenedores.operaciones.service.SeguimientoTramoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/seguimientos")
@RequiredArgsConstructor
public class SeguimientoTramoController {

    private final SeguimientoTramoService seguimientoService;

    @Operation(summary = "Registrar un nuevo evento de seguimiento de tramo")
    @PostMapping
    public ResponseEntity<SeguimientoTramo> registrar(@RequestBody SeguimientoTramoRequest dto) {
        SeguimientoTramo evento = new SeguimientoTramo();
        evento.setTramo(new Tramo());
        evento.getTramo().setId(dto.tramoId());
        evento.setEvento(dto.evento());
        evento.setLat(dto.lat());
        evento.setLng(dto.lng());
        evento.setNotas(dto.notas());
        return ResponseEntity.ok(seguimientoService.registrarEvento(evento));
    }

    @Operation(summary = "Listar eventos de seguimiento de un tramo")
    @GetMapping("/tramo/{tramoId}")
    public ResponseEntity<List<SeguimientoTramo>> listarPorTramo(@PathVariable UUID tramoId) {
        return ResponseEntity.ok(seguimientoService.obtenerPorTramo(tramoId));
    }
}
