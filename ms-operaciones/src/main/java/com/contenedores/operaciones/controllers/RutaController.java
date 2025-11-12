package com.contenedores.operaciones.controllers;

import com.contenedores.operaciones.dto.CostoEntregaResponse;
import com.contenedores.operaciones.dto.RutaDetalleResponse;
import com.contenedores.operaciones.dto.RutaRequest;
import com.contenedores.operaciones.model.Ruta;
import com.contenedores.operaciones.service.RutaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rutas")
public class RutaController {
    private final RutaService rutaService;

    public RutaController(RutaService rutaService) {
        this.rutaService = rutaService;
    }

    /**
     * Obtiene todas las rutas (sin detalles de tramos).
     * Método: GET
     * Ruta: /rutas
     * Rol: Operador / Administrador
     */
    @GetMapping
    public ResponseEntity<List<Ruta>> obtenerTodas() {
        return ResponseEntity.ok(rutaService.findAll());
    }

    /**
     * Obtiene el detalle completo de una ruta por su ID, incluyendo todos los tramos,
     * tiempo estimado y costo estimado.
     * Método: GET
     * Ruta: /rutas/{id}
     * Rol: Operador / Administrador
     */
    @GetMapping("/{id}")
    public ResponseEntity<RutaDetalleResponse> obtenerDetallePorId(@PathVariable("id") UUID id) {
        RutaDetalleResponse detalle = rutaService.findDetalleById(id);
        return ResponseEntity.ok(detalle);
    }

    /**
     * Obtiene el detalle completo de una ruta por el ID de la solicitud asociada.
     * Útil para consultar la ruta planificada de una solicitud específica.
     * Método: GET
     * Ruta: /rutas/solicitud/{solicitudId}
     * Rol: Operador / Administrador
     */
    @GetMapping("/solicitud/{solicitudId}")
    public ResponseEntity<RutaDetalleResponse> obtenerDetallePorSolicitud(@PathVariable("solicitudId") UUID solicitudId) {
        RutaDetalleResponse detalle = rutaService.findDetalleBySolicitudId(solicitudId);
        return ResponseEntity.ok(detalle);
    }

    /**
     * Asigna una nueva ruta con todos sus tramos a una solicitud.
     * La ruta se crea con estado inicial y se vincula a la solicitud especificada.
     * Método: POST
     * Ruta: /rutas
     * Rol: Operador / Administrador
     */
    @PostMapping
    public ResponseEntity<RutaDetalleResponse> asignarRuta(@RequestBody RutaRequest request) {
        RutaDetalleResponse ruta = rutaService.createFromRequest(request);
        return ResponseEntity.status(201).body(ruta);
    }

    /**
     * Calcula el costo total de entrega para una ruta (REQ-8).
     * Incluye desglose por: recorrido (distancia × precio/km), 
     * contenedor (peso × precio/kg + volumen × precio/m3),
     * estadías en depósitos (tiempo entre tramos × costo diario).
     * 
     * Método: GET
     * Ruta: /rutas/{id}/costo
     * Query Params:
     *   - pesoKg: Peso del contenedor en kg
     *   - volumenM3: Volumen del contenedor en m3
     * Rol: Operador / Administrador
     */
    @Operation(summary = "Calcular costo total de entrega con desglose")
    @GetMapping("/{id}/costo")
    public ResponseEntity<CostoEntregaResponse> calcularCostoEntrega(
            @PathVariable("id") UUID id,
            @RequestParam(name = "pesoKg", required = false, defaultValue = "10000") BigDecimal pesoKg,
            @RequestParam(name = "volumenM3", required = false, defaultValue = "25") BigDecimal volumenM3
    ) {
        CostoEntregaResponse costo = rutaService.calcularCostoTotal(id, pesoKg, volumenM3);
        return ResponseEntity.ok(costo);
    }
}