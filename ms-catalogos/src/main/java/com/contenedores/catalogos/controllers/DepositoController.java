package com.contenedores.catalogos.controllers;

import com.contenedores.catalogos.model.Deposito;
import com.contenedores.catalogos.service.DepositoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
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
}