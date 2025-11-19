package com.contenedores.solicitudes.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.contenedores.solicitudes.dto.ContenedorPendienteResponse;
import com.contenedores.solicitudes.model.EstadoSolicitud;
import com.contenedores.solicitudes.model.Solicitud;
import com.contenedores.solicitudes.service.SolicitudService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Keycloak")
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
    public ResponseEntity<Solicitud> obtenerSolicitud(
            @Parameter(description = "UUID de la solicitud", required = true, 
                      schema = @Schema(type = "string", format = "uuid", 
                                      example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890"))
            @PathVariable("id") UUID id) {
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

    /**
     * Endpoint para consultar el estado del transporte de un contenedor por su código.
     * Método: GET
     * Ruta: /api/solicitudes/contenedor/{codigo}
     * Rol: Cliente
     */
    @GetMapping("/contenedor/{codigo}")
    public ResponseEntity<Solicitud> obtenerSolicitudPorContenedor(
            @Parameter(description = "Código del contenedor", required = true,
                      schema = @Schema(type = "string", example = "CONT-001"))
            @PathVariable("codigo") String codigo) {
        Solicitud solicitud = solicitudService.findByCodigoContenedor(codigo);
        return ResponseEntity.ok(solicitud);
    }

    /**
     * Endpoint para consultar todos los contenedores pendientes de entrega con filtros opcionales.
     * Método: GET
     * Ruta: /api/solicitudes/contenedores-pendientes
     * Query Params:
     *   - estados: Lista de estados separados por coma (ej: BORRADOR,PROGRAMADA,EN_TRANSITO)
     *   - clienteId: UUID del cliente
     *   - clienteCuit: CUIT del cliente
     *   - codigoContenedor: Código del contenedor
     *   - fechaDesde: Fecha inicio (ISO format: 2024-01-01T00:00:00)
     *   - fechaHasta: Fecha fin (ISO format: 2024-12-31T23:59:59)
     * Rol: Operador / Administrador
     */
    @GetMapping("/contenedores-pendientes")
    public ResponseEntity<List<ContenedorPendienteResponse>> consultarContenedoresPendientes(
            @RequestParam(required = false) List<EstadoSolicitud> estados,
            @RequestParam(required = false) UUID clienteId,
            @RequestParam(required = false) String clienteCuit,
            @RequestParam(required = false) String codigoContenedor,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta
    ) {
        List<Solicitud> solicitudes = solicitudService.findContenedoresPendientes(
                estados,
                clienteId,
                clienteCuit,
                codigoContenedor,
                fechaDesde,
                fechaHasta
        );
        
        List<ContenedorPendienteResponse> response = solicitudService.mapToContenedorPendienteResponse(solicitudes);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para registrar la finalización de una solicitud con datos reales.
     * Actualiza el costo real, tiempo real de entrega y estado.
     * Método: PUT
     * Ruta: /api/solicitudes/{id}/finalizar
     * Rol: Sistema / Operador
     */
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Solicitud> registrarFinalizacion(
            @Parameter(description = "UUID de la solicitud", required = true,
                      schema = @Schema(type = "string", format = "uuid",
                                      example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890"))
            @PathVariable("id") UUID solicitudId,
            @RequestParam(name = "costoReal") java.math.BigDecimal costoReal,
            @RequestParam(name = "tiempoRealEntrega") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime tiempoRealEntrega
    ) {
        Solicitud solicitudFinalizada = solicitudService.registrarFinalizacion(
                solicitudId,
                costoReal,
                tiempoRealEntrega
        );
        return ResponseEntity.ok(solicitudFinalizada);
    }
    
    /**
     * Obtener historial de estados de una solicitud (seguimiento cronológico).
     * Método: GET
     * Ruta: /api/solicitudes/{id}/historial
     * Rol: Cliente / Operador
     */
    @GetMapping("/{id}/historial")
    public ResponseEntity<List<com.contenedores.solicitudes.dto.HistorialEstadoResponse>> obtenerHistorialEstados(
            @Parameter(description = "UUID de la solicitud", required = true,
                      schema = @Schema(type = "string", format = "uuid",
                                      example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890"))
            @PathVariable("id") UUID id) {
        List<com.contenedores.solicitudes.dto.HistorialEstadoResponse> historial = solicitudService.obtenerHistorialEstados(id);
        return ResponseEntity.ok(historial);
    }
    
    /**
     * Cambiar el estado de una solicitud manualmente.
     * Método: PUT
     * Ruta: /api/solicitudes/{id}/estado
     * Rol: Operador
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<Solicitud> cambiarEstado(
            @Parameter(description = "UUID de la solicitud", required = true,
                      schema = @Schema(type = "string", format = "uuid",
                                      example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890"))
            @PathVariable("id") UUID id,
            @RequestParam(name = "nuevoEstado") EstadoSolicitud nuevoEstado,
            @RequestParam(name = "observaciones", required = false, defaultValue = "") String observaciones) {
        Solicitud solicitudActualizada = solicitudService.cambiarEstado(id, nuevoEstado, observaciones);
        return ResponseEntity.ok(solicitudActualizada);
    }
}
