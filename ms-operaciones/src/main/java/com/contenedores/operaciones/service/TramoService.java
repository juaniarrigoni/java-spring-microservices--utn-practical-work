package com.contenedores.operaciones.service;

import com.contenedores.operaciones.model.*;
import com.contenedores.operaciones.repository.AsignacionCamionRepository;
import com.contenedores.operaciones.repository.TramoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TramoService {

    private final TramoRepository tramoRepository;
    private final AsignacionCamionRepository asignacionRepository;

    public List<Tramo> listarPorRuta(UUID rutaId) {
        return tramoRepository.findByRutaId(rutaId);
    }

    public Tramo obtenerPorId(UUID id) {
        return tramoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado: " + id));
    }

    public Tramo cambiarEstado(UUID tramoId, EstadoTramo nuevoEstado) {
        Tramo tramo = obtenerPorId(tramoId);
        tramo.setEstado(nuevoEstado);
        return tramoRepository.save(tramo);
    }

    public void asignarCamion(UUID tramoId, UUID camionId) {
        // Validar que el camión no esté en uso
        boolean ocupado = asignacionRepository.findByCamionId(camionId)
                .filter(a -> a.getTramo().getEstado() == EstadoTramo.EN_CURSO)
                .isPresent();

        if (ocupado) {
            throw new RuntimeException("El camión ya está asignado a un tramo en curso.");
        }

        Tramo tramo = obtenerPorId(tramoId);

        AsignacionCamion asignacion = new AsignacionCamion();
        asignacion.setTramo(tramo);
        asignacion.setCamionId(camionId);
        asignacion.setFechaAsignacion(LocalDateTime.now());
        asignacion.setConfirmado(Boolean.TRUE);

        tramo.setAsignacion(asignacion);
        tramo.setEstado(EstadoTramo.EN_CURSO);

        tramoRepository.save(tramo);
    }
}
