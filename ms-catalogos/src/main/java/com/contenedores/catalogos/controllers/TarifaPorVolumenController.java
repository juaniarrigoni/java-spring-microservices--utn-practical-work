package com.contenedores.catalogos.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.contenedores.catalogos.dto.TarifaPorVolumenRequest;
import com.contenedores.catalogos.dto.TarifaPorVolumenResponse;
import com.contenedores.catalogos.model.TarifaPorVolumen;
import com.contenedores.catalogos.service.TarifaPorVolumenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@SecurityRequirement(name = "Keycloak")
@RequestMapping("/tarifas-volumen")
@RequiredArgsConstructor
public class TarifaPorVolumenController {

    private final TarifaPorVolumenService service;

    /**
     * GET /tarifas-volumen/costo-base?volumenM3={volumen}
     * Obtiene el costo base por km para un volumen específico
     */
    @GetMapping("/costo-base")
    public ResponseEntity<BigDecimal> obtenerCostoBase(
            @RequestParam("volumenM3") BigDecimal volumenM3) {
        BigDecimal costoBase = service.obtenerCostoBaseKmPorVolumen(volumenM3);
        return ResponseEntity.ok(costoBase);
    }

    /**
     * GET /tarifas-volumen/por-volumen?volumenM3={volumen}
     * Obtiene la tarifa completa para un volumen específico
     */
    @GetMapping("/por-volumen")
    public ResponseEntity<TarifaPorVolumenResponse> obtenerPorVolumen(
            @RequestParam("volumenM3") BigDecimal volumenM3) {
        TarifaPorVolumen tarifa = service.obtenerTarifaPorVolumen(volumenM3);
        return ResponseEntity.ok(toResponse(tarifa));
    }

    /**
     * GET /tarifas-volumen/activas
     * Lista todas las tarifas activas
     */
    @GetMapping("/activas")
    public ResponseEntity<List<TarifaPorVolumenResponse>> listarActivas() {
        List<TarifaPorVolumenResponse> tarifas = service.listarActivas().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(tarifas);
    }

    /**
     * GET /tarifas-volumen
     * Lista todas las tarifas
     */
    @GetMapping
    public ResponseEntity<List<TarifaPorVolumenResponse>> listarTodas() {
        List<TarifaPorVolumenResponse> tarifas = service.listarTodas().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(tarifas);
    }

    /**
     * GET /tarifas-volumen/{id}
     * Obtiene una tarifa por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TarifaPorVolumenResponse> obtenerPorId(@PathVariable("id") UUID id) {
        TarifaPorVolumen tarifa = service.obtenerPorId(id);
        return ResponseEntity.ok(toResponse(tarifa));
    }

    /**
     * POST /tarifas-volumen
     * Crea una nueva tarifa por volumen
     */
    @PostMapping
    public ResponseEntity<TarifaPorVolumenResponse> crear(
            @Valid @RequestBody TarifaPorVolumenRequest request) {
        TarifaPorVolumen tarifa = toEntity(request);
        TarifaPorVolumen creada = service.crear(tarifa);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(creada));
    }

    /**
     * PUT /tarifas-volumen/{id}
     * Actualiza una tarifa existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<TarifaPorVolumenResponse> actualizar(
            @PathVariable("id") UUID id,
            @Valid @RequestBody TarifaPorVolumenRequest request) {
        TarifaPorVolumen tarifa = toEntity(request);
        TarifaPorVolumen actualizada = service.actualizar(id, tarifa);
        return ResponseEntity.ok(toResponse(actualizada));
    }

    /**
     * PATCH /tarifas-volumen/{id}/estado?activa={true|false}
     * Cambia el estado activo de una tarifa
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<TarifaPorVolumenResponse> cambiarEstado(
            @PathVariable("id") UUID id,
            @RequestParam("activa") boolean activa) {
        TarifaPorVolumen tarifa = service.cambiarEstado(id, activa);
        return ResponseEntity.ok(toResponse(tarifa));
    }

    /**
     * DELETE /tarifas-volumen/{id}
     * Elimina una tarifa
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") UUID id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Mappers
    private TarifaPorVolumenResponse toResponse(TarifaPorVolumen tarifa) {
        return new TarifaPorVolumenResponse(
            tarifa.getId(),
            tarifa.getNombre(),
            tarifa.getDescripcion(),
            tarifa.getVolumenMinM3(),
            tarifa.getVolumenMaxM3(),
            tarifa.getCostoBaseKm(),
            tarifa.getActiva(),
            tarifa.getOrdenPrioridad(),
            tarifa.getVigenciaDesde(),
            tarifa.getVigenciaHasta(),
            tarifa.getFechaCreacion(),
            tarifa.getFechaModificacion()
        );
    }

    private TarifaPorVolumen toEntity(TarifaPorVolumenRequest request) {
        TarifaPorVolumen tarifa = new TarifaPorVolumen();
        tarifa.setNombre(request.nombre());
        tarifa.setDescripcion(request.descripcion());
        tarifa.setVolumenMinM3(request.volumenMinM3());
        tarifa.setVolumenMaxM3(request.volumenMaxM3());
        tarifa.setCostoBaseKm(request.costoBaseKm());
        tarifa.setActiva(request.activa() != null ? request.activa() : true);
        tarifa.setOrdenPrioridad(request.ordenPrioridad() != null ? request.ordenPrioridad() : 0);
        tarifa.setVigenciaDesde(request.vigenciaDesde());
        tarifa.setVigenciaHasta(request.vigenciaHasta());
        return tarifa;
    }
}
