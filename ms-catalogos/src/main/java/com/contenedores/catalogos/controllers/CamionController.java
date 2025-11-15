package com.contenedores.catalogos.controllers;

import com.contenedores.catalogos.model.Camion;
import com.contenedores.catalogos.service.CamionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/camiones")
public class CamionController {

    private final CamionService camionService;

    public CamionController(CamionService camionService) {
        this.camionService = camionService;
    }

    /**
     * Endpoint para obtener todos los camiones.
     * Método: GET
     * Ruta: /camiones
     */
    @GetMapping
    public ResponseEntity<List<Camion>> obtenerTodos() {
        List<Camion> camiones = camionService.findAll();
        return ResponseEntity.ok(camiones);
    }

    /**
     * Endpoint para crear un nuevo camión.
     * Acepta un objeto Camion en el cuerpo de la solicitud.
     * Método: POST
     * Ruta: /camiones
     */
    @PostMapping
    public ResponseEntity<Camion> crearCamion(@RequestBody Camion camion) {
        Camion camionCreado = camionService.create(camion);

        // Construimos la URI del nuevo recurso para devolverla en el header 'Location'
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(camionCreado.getId())
                .toUri();

        return ResponseEntity.created(location).body(camionCreado);
    }

    /**
     * Endpoint para obtener un camión por ID.
     * Método: GET
     * Ruta: /camiones/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Camion> obtenerPorId(@PathVariable("id") java.util.UUID id) {
        Camion camion = camionService.findById(id);
        return ResponseEntity.ok(camion);
    }

    /**
     * Endpoint para actualizar un camión existente.
     * Método: PUT
     * Ruta: /camiones/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Camion> actualizarCamion(
            @PathVariable("id") java.util.UUID id,
            @RequestBody Camion camion) {
        Camion camionActualizado = camionService.update(id, camion);
        return ResponseEntity.ok(camionActualizado);
    }

    /**
     * Endpoint para eliminar (desactivar) un camión.
     * Método: DELETE
     * Ruta: /camiones/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCamion(@PathVariable("id") java.util.UUID id) {
        camionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
