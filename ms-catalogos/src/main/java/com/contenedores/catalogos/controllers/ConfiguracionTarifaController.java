package com.contenedores.catalogos.controllers;

import com.contenedores.catalogos.dto.ConfiguracionTarifaRequest;
import com.contenedores.catalogos.dto.ConfiguracionTarifaResponse;
import com.contenedores.catalogos.model.ConfiguracionTarifa;
import com.contenedores.catalogos.service.ConfiguracionTarifaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/configuracion-tarifas")
@RequiredArgsConstructor
public class ConfiguracionTarifaController {

    private final ConfiguracionTarifaService service;

    /**
     * GET /configuracion-tarifas/activa
     * Obtiene la configuración de tarifa activa
     */
    @GetMapping("/activa")
    public ResponseEntity<ConfiguracionTarifaResponse> obtenerActiva() {
        ConfiguracionTarifa config = service.obtenerConfiguracionActiva();
        return ResponseEntity.ok(toResponse(config));
    }

    /**
     * GET /configuracion-tarifas
     * Lista todas las configuraciones
     */
    @GetMapping
    public ResponseEntity<List<ConfiguracionTarifaResponse>> listarTodas() {
        List<ConfiguracionTarifaResponse> configs = service.listarTodas().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(configs);
    }

    /**
     * GET /configuracion-tarifas/{id}
     * Obtiene una configuración por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConfiguracionTarifaResponse> obtenerPorId(@PathVariable UUID id) {
        ConfiguracionTarifa config = service.obtenerPorId(id);
        return ResponseEntity.ok(toResponse(config));
    }

    /**
     * POST /configuracion-tarifas
     * Crea una nueva configuración
     */
    @PostMapping
    public ResponseEntity<ConfiguracionTarifaResponse> crear(
            @Valid @RequestBody ConfiguracionTarifaRequest request) {
        ConfiguracionTarifa config = toEntity(request);
        ConfiguracionTarifa creada = service.crear(config);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(creada));
    }

    /**
     * PUT /configuracion-tarifas/{id}
     * Actualiza una configuración existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<ConfiguracionTarifaResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody ConfiguracionTarifaRequest request) {
        ConfiguracionTarifa config = toEntity(request);
        ConfiguracionTarifa actualizada = service.actualizar(id, config);
        return ResponseEntity.ok(toResponse(actualizada));
    }

    /**
     * POST /configuracion-tarifas/{id}/activar
     * Activa una configuración específica
     */
    @PostMapping("/{id}/activar")
    public ResponseEntity<ConfiguracionTarifaResponse> activar(@PathVariable UUID id) {
        ConfiguracionTarifa activada = service.activar(id);
        return ResponseEntity.ok(toResponse(activada));
    }

    /**
     * DELETE /configuracion-tarifas/{id}
     * Elimina una configuración (solo si no está activa)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Mappers
    private ConfiguracionTarifaResponse toResponse(ConfiguracionTarifa config) {
        return new ConfiguracionTarifaResponse(
            config.getId(),
            config.getNombre(),
            config.getDescripcion(),
            config.getPrecioLitroCombustible(),
            config.getCargoGestionPorTramo(),
            config.getVelocidadPromedioKmH(),
            config.getCostoEstadiaDiarioDefault(),
            config.getActiva(),
            config.getVigenciaDesde(),
            config.getVigenciaHasta(),
            config.getFechaCreacion(),
            config.getFechaModificacion()
        );
    }

    private ConfiguracionTarifa toEntity(ConfiguracionTarifaRequest request) {
        ConfiguracionTarifa config = new ConfiguracionTarifa();
        config.setNombre(request.nombre());
        config.setDescripcion(request.descripcion());
        config.setPrecioLitroCombustible(request.precioLitroCombustible());
        config.setCargoGestionPorTramo(request.cargoGestionPorTramo());
        config.setVelocidadPromedioKmH(request.velocidadPromedioKmH());
        config.setCostoEstadiaDiarioDefault(request.costoEstadiaDiarioDefault());
        config.setActiva(request.activa() != null ? request.activa() : true);
        config.setVigenciaDesde(request.vigenciaDesde());
        config.setVigenciaHasta(request.vigenciaHasta());
        return config;
    }
}
