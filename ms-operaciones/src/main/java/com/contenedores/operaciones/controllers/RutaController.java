package com.contenedores.operaciones.controllers;

import com.contenedores.operaciones.dto.RutaRequest;
import com.contenedores.operaciones.dto.TramoRequest;
import com.contenedores.operaciones.model.*;
import com.contenedores.operaciones.service.RutaService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rutas")
@RequiredArgsConstructor
public class RutaController {

    private final RutaService rutaService;

    @Operation(summary = "Crear una nueva ruta con sus tramos")
    @PostMapping
    public ResponseEntity<Ruta> crear(@RequestBody RutaRequest dto) {
        Ruta ruta = new Ruta();
        ruta.setSolicitudId(dto.solicitudId());
        ruta.setDistanciaKmPlan(dto.distanciaKmPlan());
        ruta.setDuracionMinPlan(dto.duracionMinPlan());

        List<Tramo> tramos = dto.tramos().stream().map((TramoRequest t) -> {
            Tramo tramo = new Tramo();
            tramo.setRuta(ruta);
            tramo.setOrden(t.orden());
            tramo.setOrigenNombre(t.origenNombre());
            tramo.setOrigenLat(t.origenLat());
            tramo.setOrigenLng(t.origenLng());
            tramo.setDestinoNombre(t.destinoNombre());
            tramo.setDestinoLat(t.destinoLat());
            tramo.setDestinoLng(t.destinoLng());
            tramo.setDistanciaKmPlan(t.distanciaKmPlan());
            tramo.setDuracionMinPlan(t.duracionMinPlan());
            tramo.setEstado(EstadoTramo.PENDIENTE);
            return tramo;
        }).toList();

        ruta.setTramos(tramos);
        return ResponseEntity.ok(rutaService.crearRuta(ruta));
    }

    @Operation(summary = "Listar todas las rutas")
    @GetMapping
    public ResponseEntity<List<Ruta>> listar() {
        return ResponseEntity.ok(rutaService.listarRutas());
    }

    @Operation(summary = "Obtener una ruta por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Ruta> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(rutaService.obtenerPorId(id));
    }
}
