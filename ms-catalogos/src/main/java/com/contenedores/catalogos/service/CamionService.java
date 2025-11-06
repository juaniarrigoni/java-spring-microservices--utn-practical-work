package com.contenedores.catalogos.service;

import com.contenedores.catalogos.dto.CamionRequest;
import com.contenedores.catalogos.model.Camion;
import com.contenedores.catalogos.repository.CamionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CamionService {

    private final CamionRepository camionRepository;

    public CamionService(CamionRepository camionRepository) {
        this.camionRepository = camionRepository;
    }

    public List<Camion> findAll() {
        return camionRepository.findAll();
    }

    public Camion create(CamionRequest request) {
        validarCamion(request);

        Camion c = new Camion();
        c.setPatente(request.getPatente().trim().toUpperCase());
        c.setCapacidadKg(request.getCapacidadKg());
        c.setVolumenM3(request.getVolumenM3());
        c.setTipo(request.getTipo().trim());
        c.setActivo(true);

        return camionRepository.save(c);
    }

    // Reglas de negocio
    private void validarCamion(CamionRequest request) {
        if (request.getPatente() == null || request.getPatente().isBlank()) {
            throw new IllegalArgumentException("La patente del camión es obligatoria.");
        }

        if (camionRepository.existsByPatente(request.getPatente().trim().toUpperCase())) {
            throw new IllegalStateException("Ya existe un camión con la patente " + request.getPatente());
        }

        if (request.getCapacidadKg() == null || request.getCapacidadKg() <= 0) {
            throw new IllegalArgumentException("La capacidad en kg debe ser mayor que cero.");
        }

        if (request.getVolumenM3() == null || request.getVolumenM3() <= 0) {
            throw new IllegalArgumentException("El volumen en m³ debe ser mayor que cero.");
        }

        if (request.getTipo() == null || request.getTipo().isBlank()) {
            throw new IllegalArgumentException("El tipo de camión es obligatorio.");
        }
    }
}
