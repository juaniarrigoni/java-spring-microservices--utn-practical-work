package com.contenedores.operaciones.service;

import com.contenedores.operaciones.model.AsignacionCamion;
import com.contenedores.operaciones.model.Tramo;
import com.contenedores.operaciones.repository.AsignacionCamionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor // Inyecta las dependencias marcadas como 'final'
@Transactional
public class AsignacionCamionService {

    private final AsignacionCamionRepository asignacionRepository;

    /**
     * Busca todas las asignaciones asociadas a un ID de camión.
     * Un camión puede tener varias asignaciones si se le planifican varios tramos.
     * @param camionId El UUID del camión.
     * @return Una lista de asignaciones para ese camión.
     */
    public List<AsignacionCamion> buscarPorCamion(UUID camionId) {
        return asignacionRepository.findByCamionId(camionId);
    }

    /**
     * Crea y guarda una nueva asignación de un camión a un tramo.
     * Este método sería llamado internamente, por ejemplo, desde TramoService.
     * @param tramo El tramo al que se asignará el camión.
     * @param camionId El ID del camión a asignar.
     * @return La entidad AsignacionCamion creada.
     */
    public AsignacionCamion crearAsignacion(Tramo tramo, UUID camionId) {
        if (tramo == null) {
            throw new IllegalArgumentException("El tramo no puede ser nulo para crear una asignación.");
        }

        AsignacionCamion asignacion = AsignacionCamion.builder()
                .tramo(tramo)
                .camionId(camionId)
                .build();

        return asignacionRepository.save(asignacion);
    }

    /**
     * Confirma una asignación. Esto lo haría el transportista.
     * @param asignacionId El ID de la asignación a confirmar.
     * @return La asignación confirmada.
     */
    public AsignacionCamion confirmarAsignacion(UUID asignacionId) {
        AsignacionCamion asignacion = asignacionRepository.findById(asignacionId)
                .orElseThrow(() -> new EntityNotFoundException("Asignación no encontrada con ID: " + asignacionId));

        asignacion.setConfirmado(true);
        return asignacionRepository.save(asignacion);
    }
}