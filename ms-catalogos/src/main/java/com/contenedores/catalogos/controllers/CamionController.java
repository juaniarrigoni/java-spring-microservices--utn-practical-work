package com.contenedores.catalogos.controllers;

import com.contenedores.catalogos.dto.CamionRequest;
import com.contenedores.catalogos.model.Camion;
import com.contenedores.catalogos.service.CamionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/camiones")
public class CamionController {

    private final CamionService camionService;

    public CamionController(CamionService camionService) {
        this.camionService = camionService;
    }

    @GetMapping
    public ResponseEntity<List<Camion>> listarCamiones() {
        return ResponseEntity.ok(camionService.findAll());
    }

    @PostMapping
    public ResponseEntity<Camion> registrarCamion(@RequestBody CamionRequest request) {
        return ResponseEntity.ok(camionService.create(request));
    }
}
