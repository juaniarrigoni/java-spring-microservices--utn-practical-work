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
}