package com.contenedores.operaciones.controllers;

import com.contenedores.operaciones.model.Ruta;
import com.contenedores.operaciones.service.RutaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/rutas")
public class RutaController {
    private final RutaService rutaService;

    public RutaController(RutaService rutaService) {
        this.rutaService = rutaService;
    }

    @GetMapping
    public ResponseEntity<List<Ruta>> obtenerTodas() {
        return ResponseEntity.ok(rutaService.findAll());
    }

    @PostMapping
    public ResponseEntity<Ruta> crearRuta(@RequestBody Ruta ruta) {
        return ResponseEntity.ok(rutaService.create(ruta));
    }
}