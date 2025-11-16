package com.contenedores.operaciones.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement; // agregado
import org.springframework.web.bind.annotation.RestController; // agregado

import com.contenedores.operaciones.model.SeguimientoTramo;
import com.contenedores.operaciones.model.TipoEventoSeguimiento; // Importar el Enum
import com.contenedores.operaciones.service.SeguimientoTramoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Keycloak")
@RequestMapping("/seguimiento")
public class SeguimientoTramoController {
    private final SeguimientoTramoService seguimientoService;

    public SeguimientoTramoController(SeguimientoTramoService seguimientoService) {
        this.seguimientoService = seguimientoService;
    }

    // Este es un ejemplo de cómo podría ser el endpoint
    @PostMapping
    public ResponseEntity<SeguimientoTramo> registrarEvento(@RequestBody SeguimientoTramo evento) {
        // El body del request ya vendría con el tramoId, evento, latitud, longitud, etc.
        // Spring se encarga de convertir el string "INICIO" al enum TipoEventoSeguimiento.INICIO
        SeguimientoTramo eventoRegistrado = seguimientoService.registrarEvento(evento);
        return ResponseEntity.ok(eventoRegistrado);
    }
}