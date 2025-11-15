package com.contenedores.operaciones.service;

import com.contenedores.operaciones.dto.TramoEstadoResponse;
import com.contenedores.operaciones.model.AsignacionCamion;
import com.contenedores.operaciones.model.EstadoTramo;
import com.contenedores.operaciones.model.Tramo;
import com.contenedores.operaciones.repository.AsignacionCamionRepository;
import com.contenedores.operaciones.repository.TramoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * Inicia un tramo de traslado (REQ-7).
     * Valida que el tramo esté en estado PENDIENTE y tenga un camión asignado.
     * Cambia el estado a EN_CURSO y registra la fecha/hora de inicio.
     * @param tramoId UUID del tramo a iniciar
     * @return DTO con el estado actualizado del tramo
     */
    @Transactional
    public TramoEstadoResponse iniciarTramo(UUID tramoId) {
        Tramo tramo = tramoRepository.findById(tramoId)
                .orElseThrow(() -> new EntityNotFoundException("Tramo no encontrado con ID: " + tramoId));

        // Validación 1: El tramo debe estar en estado PENDIENTE
        if (tramo.getEstado() != EstadoTramo.PENDIENTE) {
            throw new IllegalStateException("El tramo debe estar en estado PENDIENTE para iniciarse. Estado actual: " + tramo.getEstado());
        }

        // Validación 2: El tramo debe tener un camión asignado
        if (tramo.getAsignacionCamion() == null) {
            throw new IllegalStateException("El tramo no tiene un camión asignado. Debe asignarse un camión antes de iniciar el tramo.");
        }

        // Cambiar estado y registrar fecha de inicio
        tramo.setEstado(EstadoTramo.EN_CURSO);
        tramo.setFechaInicioReal(LocalDateTime.now());
        Tramo tramoActualizado = tramoRepository.save(tramo);
        
        return mapToTramoEstadoResponse(tramoActualizado);
    }

    /**
     * Finaliza un tramo de traslado (REQ-7).
     * Valida que el tramo esté en estado EN_CURSO.
     * Cambia el estado a COMPLETADO y registra la fecha/hora de finalización.
     * @param tramoId UUID del tramo a finalizar
     * @return DTO con el estado actualizado del tramo
     */
    @Transactional
    public TramoEstadoResponse finalizarTramo(UUID tramoId) {
        Tramo tramo = tramoRepository.findById(tramoId)
                .orElseThrow(() -> new EntityNotFoundException("Tramo no encontrado con ID: " + tramoId));

        // Validación: El tramo debe estar en estado EN_CURSO
        if (tramo.getEstado() != EstadoTramo.EN_CURSO) {
            throw new IllegalStateException("El tramo debe estar en estado EN_CURSO para finalizarse. Estado actual: " + tramo.getEstado());
        }

        // Cambiar estado y registrar fecha de finalización
        tramo.setEstado(EstadoTramo.COMPLETADO);
        tramo.setFechaFinReal(LocalDateTime.now());
        Tramo tramoActualizado = tramoRepository.save(tramo);
        
        return mapToTramoEstadoResponse(tramoActualizado);
    }

    /**
     * Mapea una entidad Tramo a TramoEstadoResponse.
     * Incluye información de la asignación de camión si existe.
     */
    private TramoEstadoResponse mapToTramoEstadoResponse(Tramo tramo) {
        UUID camionId = null;
        Boolean confirmado = null;
        
        if (tramo.getAsignacionCamion() != null) {
            camionId = tramo.getAsignacionCamion().getCamionId();
            confirmado = tramo.getAsignacionCamion().getConfirmado();
        }
        
        return new TramoEstadoResponse(
                tramo.getId(),
                tramo.getOrden(),
                tramo.getOrigenNombre(),
                tramo.getDestinoNombre(),
                tramo.getDistanciaKmPlan(),
                tramo.getDuracionMinPlan(),
                tramo.getEstado(),
                tramo.getFechaInicioReal(),
                tramo.getFechaFinReal(),
                camionId,
                confirmado
        );
    }
}