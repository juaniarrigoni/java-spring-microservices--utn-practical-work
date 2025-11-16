package com.contenedores.catalogos.controllers;

import com.contenedores.catalogos.model.Tarifa;
import com.contenedores.catalogos.service.TarifaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@SecurityRequirement(name = "Keycloak")
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

    /**
     * Endpoint para obtener una tarifa por ID.
     * Método: GET
     * Ruta: /tarifas/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Tarifa> obtenerPorId(@PathVariable("id") java.util.UUID id) {
        Tarifa tarifa = tarifaService.findById(id);
        return ResponseEntity.ok(tarifa);
    }

    /**
     * Endpoint para actualizar una tarifa existente.
     * Método: PUT
     * Ruta: /tarifas/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Tarifa> actualizarTarifa(
            @PathVariable("id") java.util.UUID id,
            @RequestBody Tarifa tarifa) {
        Tarifa tarifaActualizada = tarifaService.update(id, tarifa);
        return ResponseEntity.ok(tarifaActualizada);
    }

    /**
     * Endpoint para eliminar (desactivar) una tarifa.
     * Método: DELETE
     * Ruta: /tarifas/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTarifa(@PathVariable("id") java.util.UUID id) {
        tarifaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}