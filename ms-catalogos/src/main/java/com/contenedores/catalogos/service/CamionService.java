package com.contenedores.catalogos.service;

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

    /**
     * Crea un nuevo camión en el sistema.
     * Valida que no exista otro camión con la misma patente.
     * @param camion La entidad Camion a guardar.
     * @return La entidad Camion guardada con su ID asignado.
     */
    public Camion create(Camion camion) {
        if (camionRepository.existsByPatente(camion.getPatente().trim())) {
            throw new IllegalArgumentException("Ya existe un camión con la patente: " + camion.getPatente());
        }
        // Aseguramos que los valores por defecto se apliquen si son nulos
        camion.setActivo(camion.getActivo() == null ? true : camion.getActivo());
        camion.setDisponible(camion.getDisponible() == null ? true : camion.getDisponible());

        return camionRepository.save(camion);
    }

    /**
     * Obtiene una lista de todos los camiones registrados.
     * @return Una lista de entidades Camion.
     */
    public List<Camion> findAll() {
        return camionRepository.findAll();
    }
}