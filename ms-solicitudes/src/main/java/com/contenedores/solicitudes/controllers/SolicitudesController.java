package com.contenedores.solicitudes.controllers;

import com.contenedores.solicitudes.model.Solicitud;
import com.contenedores.solicitudes.service.SolicitudService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudesController {

    private final SolicitudService solicitudService;

    public SolicitudesController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    /**
     * Endpoint para crear una nueva solicitud.
     * Método: POST
     * Ruta: /api/solicitudes
     * Rol: Cliente
     */
    @PostMapping
    public ResponseEntity<Solicitud> crearSolicitud(@RequestBody Solicitud solicitud) {
        Solicitud solicitudCreada = solicitudService.create(solicitud);

        // Construimos la URI del nuevo recurso para devolverla en el header 'Location'
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(solicitudCreada.getId())
                .toUri();

        return ResponseEntity.created(location).body(solicitudCreada);
    }

    /**
     * Endpoint para obtener una solicitud por ID.
     * Método: GET
     * Ruta: /api/solicitudes/{id}
     * Rol: Cliente / Operador
     */
    @GetMapping("/{id}")
    public ResponseEntity<Solicitud> obtenerSolicitud(@PathVariable UUID id) {
        Solicitud solicitud = solicitudService.findById(id);
        return ResponseEntity.ok(solicitud);
    }

    /**
     * Endpoint para listar solicitudes pendientes.
     * Método: GET
     * Ruta: /api/solicitudes/pendientes
     * Rol: Operador
     */
    @GetMapping("/pendientes")
    public ResponseEntity<List<Solicitud>> listarPendientes() {
        List<Solicitud> solicitudesPendientes = solicitudService.findPendientes();
        return ResponseEntity.ok(solicitudesPendientes);
    }
}
