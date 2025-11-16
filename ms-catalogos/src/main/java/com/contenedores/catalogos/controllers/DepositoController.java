package com.contenedores.catalogos.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.contenedores.catalogos.model.Deposito;
import com.contenedores.catalogos.service.DepositoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@SecurityRequirement(name = "Keycloak")
@RequestMapping("/depositos")
public class DepositoController {

    private final DepositoService depositoService;

    public DepositoController(DepositoService depositoService) {
        this.depositoService = depositoService;
    }

    /**
     * Endpoint para obtener todos los depósitos.
     * Método: GET
     * Ruta: /depositos
     */
    @GetMapping
    public ResponseEntity<List<Deposito>> obtenerTodos() {
        List<Deposito> depositos = depositoService.findAll();
        return ResponseEntity.ok(depositos);
    }

    /**
     * Endpoint para crear un nuevo depósito.
     * Acepta un objeto Deposito en el cuerpo de la solicitud.
     * Método: POST
     * Ruta: /depositos
     */
    @PostMapping
    public ResponseEntity<Deposito> crearDeposito(@RequestBody Deposito deposito) {
        Deposito depositoCreado = depositoService.create(deposito);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(depositoCreado.getId())
                .toUri();

        return ResponseEntity.created(location).body(depositoCreado);
    }

    /**
     * Endpoint para obtener un depósito por ID.
     * Método: GET
     * Ruta: /depositos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Deposito> obtenerPorId(@PathVariable("id") java.util.UUID id) {
        Deposito deposito = depositoService.findById(id);
        return ResponseEntity.ok(deposito);
    }

    /**
     * Endpoint para actualizar un depósito existente.
     * Método: PUT
     * Ruta: /depositos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Deposito> actualizarDeposito(
            @PathVariable("id") java.util.UUID id,
            @RequestBody Deposito deposito) {
        Deposito depositoActualizado = depositoService.update(id, deposito);
        return ResponseEntity.ok(depositoActualizado);
    }

    /**
     * Endpoint para eliminar un depósito.
     * Método: DELETE
     * Ruta: /depositos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDeposito(@PathVariable("id") java.util.UUID id) {
        depositoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}