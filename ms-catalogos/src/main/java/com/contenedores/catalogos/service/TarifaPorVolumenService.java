package com.contenedores.catalogos.service;

import com.contenedores.catalogos.model.TarifaPorVolumen;
import com.contenedores.catalogos.repository.TarifaPorVolumenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TarifaPorVolumenService {

    private final TarifaPorVolumenRepository repository;

    /**
     * Obtiene el costo base por km para un volumen específico
     * Implementa la regla de negocio: "El costo por km depende del volumen del contenedor"
     */
    public BigDecimal obtenerCostoBaseKmPorVolumen(BigDecimal volumenM3) {
        List<TarifaPorVolumen> tarifas = repository.findByVolumenContenedor(volumenM3);
        
        if (tarifas.isEmpty()) {
            throw new EntityNotFoundException(
                "No existe una tarifa definida para contenedores de " + volumenM3 + " m³");
        }
        
        // Tomar la primera (mayor prioridad)
        TarifaPorVolumen tarifa = tarifas.get(0);
        log.debug("Tarifa encontrada para volumen {}: {} ({})", 
            volumenM3, tarifa.getCostoBaseKm(), tarifa.getNombre());
        
        return tarifa.getCostoBaseKm();
    }

    /**
     * Obtiene la tarifa completa aplicable para un volumen
     */
    public TarifaPorVolumen obtenerTarifaPorVolumen(BigDecimal volumenM3) {
        List<TarifaPorVolumen> tarifas = repository.findByVolumenContenedor(volumenM3);
        
        if (tarifas.isEmpty()) {
            throw new EntityNotFoundException(
                "No existe una tarifa definida para contenedores de " + volumenM3 + " m³");
        }
        
        return tarifas.get(0);
    }

    /**
     * Obtiene la tarifa aplicable en una fecha específica
     */
    public TarifaPorVolumen obtenerTarifaPorVolumenYFecha(BigDecimal volumenM3, LocalDateTime fecha) {
        LocalDateTime fechaFinal = (fecha == null) ? LocalDateTime.now() : fecha;
        List<TarifaPorVolumen> tarifas = repository.findByVolumenYFecha(volumenM3, fechaFinal);
        
        if (tarifas.isEmpty()) {
            throw new EntityNotFoundException(
                "No existe una tarifa definida para contenedores de " + volumenM3 + 
                " m³ en la fecha " + fechaFinal);
        }
        
        return tarifas.get(0);
    }

    /**
     * Crea una nueva tarifa por volumen
     */
    @Transactional
    public TarifaPorVolumen crear(TarifaPorVolumen tarifa) {
        log.info("Creando nueva tarifa por volumen: {}", tarifa.getNombre());

        validarRango(tarifa);
        validarDuplicadoActivo(tarifa, null);
        verificarSolapamiento(tarifa);

        return repository.save(tarifa);
    }

    /**
     * Actualiza una tarifa existente
     */
    @Transactional
    public TarifaPorVolumen actualizar(UUID id, TarifaPorVolumen tarifaActualizada) {
        TarifaPorVolumen existente = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Tarifa no encontrada con id: " + id));
        
        log.info("Actualizando tarifa por volumen: {}", id);

        validarRango(tarifaActualizada);
        validarDuplicadoActivo(tarifaActualizada, id);

        existente.setNombre(tarifaActualizada.getNombre());
        existente.setDescripcion(tarifaActualizada.getDescripcion());
        existente.setVolumenMinM3(tarifaActualizada.getVolumenMinM3());
        existente.setVolumenMaxM3(tarifaActualizada.getVolumenMaxM3());
        existente.setCostoBaseKm(tarifaActualizada.getCostoBaseKm());
        existente.setActiva(tarifaActualizada.getActiva());
        existente.setOrdenPrioridad(tarifaActualizada.getOrdenPrioridad());
        existente.setVigenciaDesde(tarifaActualizada.getVigenciaDesde());
        existente.setVigenciaHasta(tarifaActualizada.getVigenciaHasta());
        
        return repository.save(existente);
    }

    /**
     * Cambia el estado activo de una tarifa
     */
    @Transactional
    public TarifaPorVolumen cambiarEstado(UUID id, boolean activa) {
        TarifaPorVolumen tarifa = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Tarifa no encontrada con id: " + id));
        
        log.info("Cambiando estado de tarifa {} a {}", id, activa);
        
        tarifa.setActiva(activa);
        if (!activa) {
            tarifa.setVigenciaHasta(LocalDateTime.now());
        }
        
        return repository.save(tarifa);
    }

    /**
     * Elimina una tarifa
     */
    @Transactional
    public void eliminar(UUID id) {
        TarifaPorVolumen tarifa = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Tarifa no encontrada con id: " + id));
        
        log.info("Eliminando tarifa por volumen: {}", id);
        repository.delete(tarifa);
    }

    /**
     * Lista todas las tarifas activas
     */
    public List<TarifaPorVolumen> listarActivas() {
        return repository.findByActivaTrueOrderByOrdenPrioridadAsc();
    }

    /**
     * Lista todas las tarifas
     */
    public List<TarifaPorVolumen> listarTodas() {
        return repository.findAll();
    }

    /**
     * Obtiene una tarifa por ID
     */
    public TarifaPorVolumen obtenerPorId(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Tarifa no encontrada con id: " + id));
    }

    /**
     * Valida que el rango de volumen sea coherente
     */
    private void validarRango(TarifaPorVolumen tarifa) {
        if (tarifa.getVolumenMaxM3() != null && 
            tarifa.getVolumenMinM3().compareTo(tarifa.getVolumenMaxM3()) >= 0) {
            throw new IllegalArgumentException(
                "El volumen mínimo debe ser menor que el volumen máximo");
        }
    }

    /**
     * Verifica que no haya solapamiento con otras tarifas activas
     */
    private void verificarSolapamiento(TarifaPorVolumen tarifa) {
        BigDecimal max = tarifa.getVolumenMaxM3() != null
            ? tarifa.getVolumenMaxM3()
            : new BigDecimal("999999.99");
        
        List<TarifaPorVolumen> solapadas = repository.findSolapamientoRango(
            tarifa.getVolumenMinM3(), max);
        
        if (!solapadas.isEmpty()) {
            log.warn("Advertencia: La nueva tarifa se solapa con {} tarifas existentes",
                solapadas.size());
        }
    }

    /**
     * Valida que no exista ya una tarifa activa con el mismo rango exacto
     */
    private void validarDuplicadoActivo(TarifaPorVolumen tarifa, UUID excluirId) {
        boolean existeDuplicado = repository.existeTarifaActivaMismoRango(
            tarifa.getVolumenMinM3(),
            tarifa.getVolumenMaxM3(),
            excluirId
        );

        if (existeDuplicado) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Ya existe una tarifa activa para el rango especificado"
            );
        }
    }
}
