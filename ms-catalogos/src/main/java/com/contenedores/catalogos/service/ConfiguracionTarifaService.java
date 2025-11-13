package com.contenedores.catalogos.service;

import com.contenedores.catalogos.model.ConfiguracionTarifa;
import com.contenedores.catalogos.repository.ConfiguracionTarifaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfiguracionTarifaService {

    private final ConfiguracionTarifaRepository repository;

    /**
     * Obtiene la configuración de tarifa activa actual
     */
    public ConfiguracionTarifa obtenerConfiguracionActiva() {
        return repository.findByActivaTrue()
            .orElseThrow(() -> new EntityNotFoundException("No existe una configuración de tarifa activa"));
    }

    /**
     * Obtiene la configuración vigente en una fecha específica
     */
    public ConfiguracionTarifa obtenerConfiguracionVigenteEn(LocalDateTime fecha) {
        LocalDateTime fechaFinal = (fecha == null) ? LocalDateTime.now() : fecha;
        return repository.findVigenteEnFecha(fechaFinal)
            .orElseThrow(() -> new EntityNotFoundException(
                "No existe una configuración de tarifa vigente para la fecha: " + fechaFinal));
    }

    /**
     * Crea una nueva configuración de tarifa
     * Si se marca como activa, desactiva automáticamente la anterior
     */
    @Transactional
    public ConfiguracionTarifa crear(ConfiguracionTarifa configuracion) {
        log.info("Creando nueva configuración de tarifa: {}", configuracion.getNombre());
        
        if (configuracion.getActiva() != null && configuracion.getActiva()) {
            desactivarConfiguracionActiva();
        }
        
        return repository.save(configuracion);
    }

    /**
     * Actualiza una configuración existente
     */
    @Transactional
    public ConfiguracionTarifa actualizar(UUID id, ConfiguracionTarifa configuracionActualizada) {
        ConfiguracionTarifa existente = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Configuración no encontrada con id: " + id));
        
        log.info("Actualizando configuración de tarifa: {}", id);
        
        // Si se está activando esta configuración, desactivar la actual
        if (configuracionActualizada.getActiva() && !existente.getActiva()) {
            desactivarConfiguracionActiva();
        }
        
        existente.setNombre(configuracionActualizada.getNombre());
        existente.setDescripcion(configuracionActualizada.getDescripcion());
        existente.setPrecioLitroCombustible(configuracionActualizada.getPrecioLitroCombustible());
        existente.setCargoGestionPorTramo(configuracionActualizada.getCargoGestionPorTramo());
        existente.setVelocidadPromedioKmH(configuracionActualizada.getVelocidadPromedioKmH());
        existente.setCostoEstadiaDiarioDefault(configuracionActualizada.getCostoEstadiaDiarioDefault());
        existente.setActiva(configuracionActualizada.getActiva());
        existente.setVigenciaDesde(configuracionActualizada.getVigenciaDesde());
        existente.setVigenciaHasta(configuracionActualizada.getVigenciaHasta());
        
        return repository.save(existente);
    }

    /**
     * Activa una configuración específica
     */
    @Transactional
    public ConfiguracionTarifa activar(UUID id) {
        ConfiguracionTarifa configuracion = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Configuración no encontrada con id: " + id));
        
        log.info("Activando configuración de tarifa: {}", id);
        
        desactivarConfiguracionActiva();
        configuracion.setActiva(true);
        
        return repository.save(configuracion);
    }

    /**
     * Desactiva la configuración actual
     */
    @Transactional
    public void desactivarConfiguracionActiva() {
        repository.findByActivaTrue().ifPresent(actual -> {
            log.info("Desactivando configuración actual: {}", actual.getId());
            actual.setActiva(false);
            actual.setVigenciaHasta(LocalDateTime.now());
            repository.save(actual);
        });
    }

    /**
     * Elimina una configuración (solo si no está activa)
     */
    @Transactional
    public void eliminar(UUID id) {
        ConfiguracionTarifa configuracion = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Configuración no encontrada con id: " + id));
        
        if (configuracion.getActiva()) {
            throw new IllegalStateException("No se puede eliminar la configuración activa");
        }
        
        log.info("Eliminando configuración de tarifa: {}", id);
        repository.delete(configuracion);
    }

    /**
     * Lista todas las configuraciones
     */
    public List<ConfiguracionTarifa> listarTodas() {
        return repository.findAll();
    }

    /**
     * Obtiene una configuración por ID
     */
    public ConfiguracionTarifa obtenerPorId(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Configuración no encontrada con id: " + id));
    }
}
