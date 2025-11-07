package com.contenedores.catalogos.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.contenedores.catalogos.dto.TarifaRequest;
import com.contenedores.catalogos.dto.TarifaResponse;
import com.contenedores.catalogos.exception.BadRequestException;
import com.contenedores.catalogos.exception.ConflictException;
import com.contenedores.catalogos.exception.ResourceNotFoundException;
import com.contenedores.catalogos.model.Tarifa;
import com.contenedores.catalogos.repository.TarifaRepository;

@Service
@Transactional
public class TarifaService {

    private final TarifaRepository tarifaRepository;

    public TarifaService(TarifaRepository tarifaRepository) {
        this.tarifaRepository = tarifaRepository;
    }

    public TarifaResponse create(TarifaRequest request) {
        validateVigencia(request.getVigenciaDesde(), request.getVigenciaHasta());
        var nombre = request.getNombre().trim();
        if (nombre.isEmpty()) {
            throw new BadRequestException("El nombre de la tarifa es obligatorio");
        }
        if (tarifaRepository.existsByNombreIgnoreCase(nombre)) {
            throw new ConflictException("Ya existe una tarifa con el nombre " + nombre);
        }
        Tarifa tarifa = new Tarifa();
        tarifa.setNombre(nombre);
        tarifa.setPrecioBase(request.getPrecioBase());
        tarifa.setPrecioKm(request.getPrecioKm());
        tarifa.setPrecioKg(request.getPrecioKg());
        tarifa.setPrecioM3(request.getPrecioM3());
        tarifa.setVigenciaDesde(request.getVigenciaDesde());
        tarifa.setVigenciaHasta(request.getVigenciaHasta());
        tarifa.setActiva(true);
        return toResponse(tarifaRepository.save(tarifa));
    }

    public TarifaResponse update(Long id, TarifaRequest request) {
        validateVigencia(request.getVigenciaDesde(), request.getVigenciaHasta());
        Tarifa tarifa = findTarifa(id);
        var nombre = request.getNombre().trim();
        if (nombre.isEmpty()) {
            throw new BadRequestException("El nombre de la tarifa es obligatorio");
        }
        tarifaRepository.findByNombreIgnoreCase(nombre)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ConflictException("Ya existe una tarifa con el nombre " + nombre);
                });
        tarifa.setNombre(nombre);
        tarifa.setPrecioBase(request.getPrecioBase());
        tarifa.setPrecioKm(request.getPrecioKm());
        tarifa.setPrecioKg(request.getPrecioKg());
        tarifa.setPrecioM3(request.getPrecioM3());
        tarifa.setVigenciaDesde(request.getVigenciaDesde());
        tarifa.setVigenciaHasta(request.getVigenciaHasta());
        return toResponse(tarifaRepository.save(tarifa));
    }

    public List<TarifaResponse> list() {
        return tarifaRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public TarifaResponse get(Long id) {
        return toResponse(findTarifa(id));
    }

    public void delete(Long id) {
        tarifaRepository.delete(findTarifa(id));
    }

    public TarifaResponse activate(Long id) {
        Tarifa tarifa = findTarifa(id);
        tarifa.setActiva(true);
        return toResponse(tarifaRepository.save(tarifa));
    }

    public TarifaResponse deactivate(Long id) {
        Tarifa tarifa = findTarifa(id);
        tarifa.setActiva(false);
        return toResponse(tarifaRepository.save(tarifa));
    }

    private Tarifa findTarifa(Long id) {
        return tarifaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la tarifa " + id));
    }

    private void validateVigencia(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) {
            throw new BadRequestException("Las fechas de vigencia son obligatorias");
        }
        if (hasta.isBefore(desde)) {
            throw new BadRequestException("La fecha de fin debe ser posterior o igual a la fecha de inicio");
        }
    }

    private TarifaResponse toResponse(Tarifa tarifa) {
        return new TarifaResponse(tarifa.getId(), tarifa.getNombre(), tarifa.getPrecioBase(), tarifa.getPrecioKm(),
                tarifa.getPrecioKg(), tarifa.getPrecioM3(), tarifa.getVigenciaDesde(), tarifa.getVigenciaHasta(),
                tarifa.isActiva());
    }
}
