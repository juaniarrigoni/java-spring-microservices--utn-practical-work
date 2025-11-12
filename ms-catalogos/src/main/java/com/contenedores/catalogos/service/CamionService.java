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

    /**
     * Obtiene un camión por su ID.
     * @param id El UUID del camión.
     * @return La entidad Camion encontrada.
     */
    public Camion findById(java.util.UUID id) {
        return camionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Camión no encontrado con ID: " + id));
    }

    /**
     * Actualiza un camión existente.
     * @param id El UUID del camión a actualizar.
     * @param camionActualizado Los datos actualizados del camión.
     * @return La entidad Camion actualizada.
     */
    public Camion update(java.util.UUID id, Camion camionActualizado) {
        Camion camionExistente = findById(id);
        
        // Verificar si se está cambiando la patente y si ya existe
        if (!camionExistente.getPatente().equals(camionActualizado.getPatente().trim()) &&
                camionRepository.existsByPatente(camionActualizado.getPatente().trim())) {
            throw new IllegalArgumentException("Ya existe un camión con la patente: " + camionActualizado.getPatente());
        }
        
        // Actualizar campos
        camionExistente.setPatente(camionActualizado.getPatente().trim());
        camionExistente.setTipo(camionActualizado.getTipo());
        camionExistente.setNombreTransportista(camionActualizado.getNombreTransportista());
        camionExistente.setTelefonoTransportista(camionActualizado.getTelefonoTransportista());
        camionExistente.setCapacidadKg(camionActualizado.getCapacidadKg());
        camionExistente.setVolumenM3(camionActualizado.getVolumenM3());
        camionExistente.setCostoBaseKm(camionActualizado.getCostoBaseKm());
        camionExistente.setConsumoCombustibleKm(camionActualizado.getConsumoCombustibleKm());
        camionExistente.setActivo(camionActualizado.getActivo() != null ? camionActualizado.getActivo() : camionExistente.getActivo());
        camionExistente.setDisponible(camionActualizado.getDisponible() != null ? camionActualizado.getDisponible() : camionExistente.getDisponible());
        
        return camionRepository.save(camionExistente);
    }

    /**
     * Elimina (desactiva) un camión.
     * En lugar de borrar físicamente, lo marca como inactivo.
     * @param id El UUID del camión a desactivar.
     */
    public void delete(java.util.UUID id) {
        Camion camion = findById(id);
        camion.setActivo(false);
        camion.setDisponible(false);
        camionRepository.save(camion);
    }
}