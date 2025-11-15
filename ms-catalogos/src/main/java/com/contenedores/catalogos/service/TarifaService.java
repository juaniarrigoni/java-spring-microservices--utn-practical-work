package com.contenedores.catalogos.service;

import com.contenedores.catalogos.model.Tarifa;
import com.contenedores.catalogos.repository.TarifaRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class TarifaService {

    private final TarifaRepository tarifaRepository;

    public TarifaService(TarifaRepository tarifaRepository) {
        this.tarifaRepository = tarifaRepository;
    }

    /**
     * Crea una nueva tarifa.
     * @param tarifa La entidad Tarifa a guardar.
     * @return La entidad Tarifa guardada.
     */
    public Tarifa create(Tarifa tarifa) {
        validarTarifa(tarifa);
        return tarifaRepository.save(tarifa);
    }

    /**
     * Obtiene una lista de todas las tarifas.
     * @return Una lista de entidades Tarifa.
     */
    public List<Tarifa> findAll() {
        return tarifaRepository.findAll();
    }

    /**
     * Obtiene una tarifa por su ID.
     * @param id El UUID de la tarifa.
     * @return La entidad Tarifa encontrada.
     */
    public Tarifa findById(java.util.UUID id) {
        return tarifaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarifa no encontrada con ID: " + id));
    }

    /**
     * Actualiza una tarifa existente.
     * @param id El UUID de la tarifa a actualizar.
     * @param tarifaActualizada Los datos actualizados de la tarifa.
     * @return La entidad Tarifa actualizada.
     */
    public Tarifa update(java.util.UUID id, Tarifa tarifaActualizada) {
        Tarifa tarifaExistente = findById(id);
        
        // Validar la tarifa actualizada (sin incluir la tarifa existente en la validación de solapamiento)
        validarTarifaActualizacion(tarifaActualizada, id);
        
        // Actualizar campos
        tarifaExistente.setNombre(tarifaActualizada.getNombre());
        tarifaExistente.setPrecioBase(tarifaActualizada.getPrecioBase());
        tarifaExistente.setPrecioKm(tarifaActualizada.getPrecioKm());
        tarifaExistente.setPrecioKg(tarifaActualizada.getPrecioKg());
        tarifaExistente.setPrecioM3(tarifaActualizada.getPrecioM3());
        tarifaExistente.setVigenciaDesde(tarifaActualizada.getVigenciaDesde());
        tarifaExistente.setVigenciaHasta(tarifaActualizada.getVigenciaHasta());
        tarifaExistente.setActiva(tarifaActualizada.getActiva() != null ? tarifaActualizada.getActiva() : tarifaExistente.getActiva());
        
        return tarifaRepository.save(tarifaExistente);
    }

    /**
     * Elimina (desactiva) una tarifa.
     * @param id El UUID de la tarifa a desactivar.
     */
    public void delete(java.util.UUID id) {
        Tarifa tarifa = findById(id);
        tarifa.setActiva(false);
        tarifaRepository.save(tarifa);
    }

    private void validarTarifa(Tarifa tarifa) {
        if (tarifa.getNombre() == null || tarifa.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la tarifa es obligatorio.");
        }
        if (tarifa.getPrecioBase().compareTo(BigDecimal.ZERO) <= 0 ||
                tarifa.getPrecioKm().compareTo(BigDecimal.ZERO) <= 0 ||
                tarifa.getPrecioKg().compareTo(BigDecimal.ZERO) <= 0 ||
                tarifa.getPrecioM3().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Todos los valores de precio deben ser mayores que cero.");
        }
        LocalDate desde = tarifa.getVigenciaDesde();
        LocalDate hasta = tarifa.getVigenciaHasta();
        if (desde == null || hasta == null || hasta.isBefore(desde)) {
            throw new IllegalArgumentException("Las fechas de vigencia son inválidas.");
        }
        List<Tarifa> solapadas = tarifaRepository
                .findByActivaTrueAndVigenciaDesdeBeforeAndVigenciaHastaAfter(hasta, desde);
        if (!solapadas.isEmpty()) {
            throw new IllegalStateException("Ya existe una tarifa activa que se solapa en el rango de fechas especificado.");
        }
    }

    private void validarTarifaActualizacion(Tarifa tarifa, java.util.UUID idExcluir) {
        if (tarifa.getNombre() == null || tarifa.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la tarifa es obligatorio.");
        }
        if (tarifa.getPrecioBase().compareTo(BigDecimal.ZERO) <= 0 ||
                tarifa.getPrecioKm().compareTo(BigDecimal.ZERO) <= 0 ||
                tarifa.getPrecioKg().compareTo(BigDecimal.ZERO) <= 0 ||
                tarifa.getPrecioM3().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Todos los valores de precio deben ser mayores que cero.");
        }
        LocalDate desde = tarifa.getVigenciaDesde();
        LocalDate hasta = tarifa.getVigenciaHasta();
        if (desde == null || hasta == null || hasta.isBefore(desde)) {
            throw new IllegalArgumentException("Las fechas de vigencia son inválidas.");
        }
        // Verificar solapamiento excluyendo la tarifa que se está actualizando
        List<Tarifa> solapadas = tarifaRepository
                .findByActivaTrueAndVigenciaDesdeBeforeAndVigenciaHastaAfter(hasta, desde);
        solapadas.removeIf(t -> t.getId().equals(idExcluir));
        if (!solapadas.isEmpty()) {
            throw new IllegalStateException("Ya existe una tarifa activa que se solapa en el rango de fechas especificado.");
        }
    }
}