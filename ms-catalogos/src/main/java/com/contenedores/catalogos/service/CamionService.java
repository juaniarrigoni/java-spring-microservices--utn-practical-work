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
        if (camionRepository.existsByPatente(request.getPatente())) {
            throw new RuntimeException("Ya existe un camión con la patente " + request.getPatente());
        }
        Camion c = new Camion();
        c.setPatente(request.getPatente());
        c.setCapacidadKg(request.getCapacidadKg());
        c.setVolumenM3(request.getVolumenM3());
        c.setTipo(request.getTipo());
        c.setActivo(true);
        return camionRepository.save(c);
    }
}
