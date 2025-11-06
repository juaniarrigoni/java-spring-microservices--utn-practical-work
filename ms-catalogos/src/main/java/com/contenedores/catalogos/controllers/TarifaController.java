package com.contenedores.catalogos.controllers;

import com.contenedores.catalogos.dto.TarifaRequest;
import com.contenedores.catalogos.dto.TarifaResponse;
import com.contenedores.catalogos.service.TarifaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tarifas")
public class TarifaController {

    private final TarifaService tarifaService;

    public TarifaController(TarifaService tarifaService) {
        this.tarifaService = tarifaService;
    }

    @PostMapping
    public ResponseEntity<TarifaResponse> crearTarifa(@RequestBody TarifaRequest request) {
        return ResponseEntity.ok(tarifaService.create(request));
    }
}
