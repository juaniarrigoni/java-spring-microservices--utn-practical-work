package com.contenedores.catalogos.service;

import com.contenedores.catalogos.dto.TarifaRequest;
import com.contenedores.catalogos.dto.TarifaResponse;
import com.contenedores.catalogos.model.Tarifa;
import com.contenedores.catalogos.repository.TarifaRepository;
import org.springframework.stereotype.Service;

@Service
public class TarifaService {

    private final TarifaRepository tarifaRepository;

    public TarifaService(TarifaRepository tarifaRepository) {
        this.tarifaRepository = tarifaRepository;
    }

    public TarifaResponse create(TarifaRequest request) {
        Tarifa t = Tarifa.builder()
                .nombre(request.getNombre())
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
