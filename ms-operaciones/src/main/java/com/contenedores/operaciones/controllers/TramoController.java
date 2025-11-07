package com.contenedores.operaciones.controllers;

import com.contenedores.operaciones.model.Tramo;
import com.contenedores.operaciones.service.TramoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/tramos")
public class TramoController {
    private final TramoService tramoService;

    public TramoController(TramoService tramoService) {
        this.tramoService = tramoService;
    }

    @PutMapping("/{id}/iniciar")
    public ResponseEntity<Tramo> iniciarTramo(@PathVariable UUID id) {
        return ResponseEntity.ok(tramoService.iniciarTramo(id));
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Tramo> finalizarTramo(@PathVariable UUID id) {
        return ResponseEntity.ok(tramoService.finalizarTramo(id));
    }
}