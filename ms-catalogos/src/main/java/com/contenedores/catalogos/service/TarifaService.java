package com.contenedores.catalogos.service;

import com.contenedores.catalogos.dto.TarifaRequest;
import com.contenedores.catalogos.dto.TarifaResponse;
import com.contenedores.catalogos.model.Tarifa;
import com.contenedores.catalogos.repository.TarifaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TarifaService {

    private final TarifaRepository tarifaRepository;

    public TarifaService(TarifaRepository tarifaRepository) {
        this.tarifaRepository = tarifaRepository;
    }

    public TarifaResponse create(TarifaRequest request) {
        validarTarifa(request);

        Tarifa t = Tarifa.builder()
                .nombre(request.getNombre().trim())
                .precioBase(request.getPrecioBase())
                .precioKm(request.getPrecioKm())
                .precioKg(request.getPrecioKg())
                .precioM3(request.getPrecioM3())
                .vigenciaDesde(request.getVigenciaDesde())
                .vigenciaHasta(request.getVigenciaHasta())
                .activa(true)
                .build();

        Tarifa saved = tarifaRepository.save(t);
        return toResponse(saved);
    }

    // Reglas de negocio según el enunciado
    private void validarTarifa(TarifaRequest request) {
        if (request.getNombre() == null || request.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la tarifa es obligatorio.");
        }

        if (request.getPrecioBase() == null || request.getPrecioBase() <= 0 ||
            request.getPrecioKm() == null || request.getPrecioKm() <= 0 ||
            request.getPrecioKg() == null || request.getPrecioKg() <= 0 ||
            request.getPrecioM3() == null || request.getPrecioM3() <= 0) {
            throw new IllegalArgumentException("Todos los valores de precio deben ser mayores que cero.");
        }

        LocalDate desde = request.getVigenciaDesde();
        LocalDate hasta = request.getVigenciaHasta();

        if (desde == null || hasta == null || hasta.isBefore(desde)) {
            throw new IllegalArgumentException("Las fechas de vigencia son inválidas.");
        }

        // Evitar solapamiento con otras tarifas activas
        List<Tarifa> solapadas = tarifaRepository
                .findByActivaTrueAndVigenciaDesdeBeforeAndVigenciaHastaAfter(hasta, desde);

        if (!solapadas.isEmpty()) {
            throw new IllegalStateException("Ya existe una tarifa activa que se solapa en el rango de fechas especificado.");
        }
    }

    private TarifaResponse toResponse(Tarifa t) {
        return TarifaResponse.builder()
                .id(t.getId())
                .nombre(t.getNombre())
                .precioBase(t.getPrecioBase())
                .precioKm(t.getPrecioKm())
                .precioKg(t.getPrecioKg())
                .precioM3(t.getPrecioM3())
                .vigenciaDesde(t.getVigenciaDesde())
                .vigenciaHasta(t.getVigenciaHasta())
                .activa(t.getActiva())
                .build();
    }
}
