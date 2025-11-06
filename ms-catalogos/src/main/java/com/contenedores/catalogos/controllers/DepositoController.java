package com.contenedores.catalogos.controllers;

import com.contenedores.catalogos.dto.DepositoResponse;
import com.contenedores.catalogos.service.DepositoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/depositos")
public class DepositoController {

    private final DepositoService depositoService;

    public DepositoController(DepositoService depositoService) {
        this.depositoService = depositoService;
    }

    @GetMapping
    public ResponseEntity<List<DepositoResponse>> listarDepositos() {
        return ResponseEntity.ok(depositoService.findAll());
    }
}
