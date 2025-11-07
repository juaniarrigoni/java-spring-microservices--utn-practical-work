package com.contenedores.catalogos.controllers;

import com.contenedores.catalogos.model.Tarifa;
import com.contenedores.catalogos.service.TarifaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tarifas")
public class TarifaController {

    private final TarifaService tarifaService;

    public TarifaController(TarifaService tarifaService) {
        this.tarifaService = tarifaService;
    }

    /**
     * Endpoint para obtener todas las tarifas.
     * Método: GET
     * Ruta: /tarifas
     */
    @GetMapping
    public ResponseEntity<List<Tarifa>> obtenerTodas() {
        List<Tarifa> tarifas = tarifaService.findAll();
        return ResponseEntity.ok(tarifas);
    }

    /**
     * Endpoint para crear una nueva tarifa.
     * Acepta un objeto Tarifa en el cuerpo de la solicitud.
     * Método: POST
     * Ruta: /tarifas
     */
    @PostMapping
    public ResponseEntity<Tarifa> crearTarifa(@RequestBody Tarifa tarifa) {
        Tarifa tarifaCreada = tarifaService.create(tarifa);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(tarifaCreada.getId())
                .toUri();

        return ResponseEntity.created(location).body(tarifaCreada);
    }
}