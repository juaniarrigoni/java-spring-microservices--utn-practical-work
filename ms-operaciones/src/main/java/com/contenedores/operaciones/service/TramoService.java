package com.contenedores.operaciones.service;

import com.contenedores.operaciones.model.AsignacionCamion;
import com.contenedores.operaciones.model.EstadoTramo;
import com.contenedores.operaciones.model.Tramo;
import com.contenedores.operaciones.repository.AsignacionCamionRepository;
import com.contenedores.operaciones.repository.TramoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TramoService {
    private final TramoRepository tramoRepository;
    private final AsignacionCamionRepository asignacionCamionRepository;

    public TramoService(TramoRepository tramoRepository, AsignacionCamionRepository asignacionCamionRepository) {
        this.tramoRepository = tramoRepository;
        this.asignacionCamionRepository = asignacionCamionRepository;
    }

    public Tramo asignarCamion(UUID tramoId, UUID camionId) {
        Tramo tramo = tramoRepository.findById(tramoId)
                .orElseThrow(() -> new EntityNotFoundException("Tramo no encontrado"));

        // Aquí iría la validación para ver si el camión está disponible
        // (requeriría una llamada a ms-catalogos)

        AsignacionCamion asignacion = AsignacionCamion.builder()
                .tramo(tramo)
                .camionId(camionId)
                .build();

        asignacionCamionRepository.save(asignacion);

        // CORREGIDO: el setter correcto es setAsignacionCamion
        tramo.setAsignacionCamion(asignacion);
        return tramo;
    }

    public Tramo iniciarTramo(UUID tramoId) {
        Tramo tramo = tramoRepository.findById(tramoId)
                .orElseThrow(() -> new EntityNotFoundException("Tramo no encontrado"));
        tramo.setEstado(EstadoTramo.EN_CURSO);
        tramo.setFechaInicioReal(LocalDateTime.now());
        return tramoRepository.save(tramo);
    }

    public Tramo finalizarTramo(UUID tramoId) {
        Tramo tramo = tramoRepository.findById(tramoId)
                .orElseThrow(() -> new EntityNotFoundException("Tramo no encontrado"));
        tramo.setEstado(EstadoTramo.COMPLETADO);
        tramo.setFechaFinReal(LocalDateTime.now());
        return tramoRepository.save(tramo);
    }
}