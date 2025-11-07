package com.contenedores.catalogos.service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.contenedores.catalogos.dto.CamionRequest;
import com.contenedores.catalogos.dto.CamionResponse;
import com.contenedores.catalogos.exception.BadRequestException;
import com.contenedores.catalogos.exception.ConflictException;
import com.contenedores.catalogos.exception.ResourceNotFoundException;
import com.contenedores.catalogos.model.Camion;
import com.contenedores.catalogos.repository.CamionRepository;

@Service
@Transactional
public class CamionService {

    private final CamionRepository camionRepository;

    public CamionService(CamionRepository camionRepository) {
        this.camionRepository = camionRepository;
    }

    public CamionResponse create(CamionRequest request) {
        var patente = normalizePatente(request.getPatente());
        if (camionRepository.existsByPatenteIgnoreCase(patente)) {
            throw new ConflictException("Ya existe un camión con la patente " + patente);
        }
        Camion camion = new Camion();
        camion.setPatente(patente);
        camion.setCapacidadKg(request.getCapacidadKg());
        camion.setVolumenM3(request.getVolumenM3());
        camion.setTipo(request.getTipo());
        camion.setActivo(true);
        return toResponse(camionRepository.save(camion));
    }

    public CamionResponse update(Long id, CamionRequest request) {
        Camion camion = findCamion(id);
        var patente = normalizePatente(request.getPatente());
        camionRepository.findByPatenteIgnoreCase(patente)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ConflictException("Ya existe un camión con la patente " + patente);
                });
        camion.setPatente(patente);
        camion.setCapacidadKg(request.getCapacidadKg());
        camion.setVolumenM3(request.getVolumenM3());
        camion.setTipo(request.getTipo());
        return toResponse(camionRepository.save(camion));
    }

    public List<CamionResponse> list() {
        return camionRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public CamionResponse get(Long id) {
        return toResponse(findCamion(id));
    }

    public void delete(Long id) {
        Camion camion = findCamion(id);
        camionRepository.delete(camion);
    }

    public CamionResponse activate(Long id) {
        Camion camion = findCamion(id);
        camion.setActivo(true);
        return toResponse(camionRepository.save(camion));
    }

    public CamionResponse deactivate(Long id) {
        Camion camion = findCamion(id);
        camion.setActivo(false);
        return toResponse(camionRepository.save(camion));
    }

    private Camion findCamion(Long id) {
        return camionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el camión " + id));
    }

    private String normalizePatente(String patente) {
        if (!StringUtils.hasText(patente)) {
            throw new BadRequestException("La patente es obligatoria");
        }
        return patente.trim().toUpperCase(Locale.ROOT);
    }

    private CamionResponse toResponse(Camion camion) {
        return new CamionResponse(camion.getId(), camion.getPatente(), camion.getCapacidadKg(),
                camion.getVolumenM3(), camion.getTipo(), camion.isActivo());
    }
}
