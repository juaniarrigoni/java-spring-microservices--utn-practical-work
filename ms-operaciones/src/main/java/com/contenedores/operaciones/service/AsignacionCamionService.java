package com.contenedores.operaciones.service;

import com.contenedores.operaciones.dto.AsignacionCamionRequest;
import com.contenedores.operaciones.dto.AsignacionCamionResponse;
import com.contenedores.operaciones.model.AsignacionCamion;
import com.contenedores.operaciones.model.EstadoTramo;
import com.contenedores.operaciones.model.Tramo;
import com.contenedores.operaciones.repository.AsignacionCamionRepository;
import com.contenedores.operaciones.repository.TramoRepository;
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
    private final TramoRepository tramoRepository;

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

    /**
     * Asigna un camión a un tramo específico (REQ-6).
     * Valida que el tramo exista, no tenga asignación previa, y esté en estado PENDIENTE.
     * En un sistema real, se validaría también que el camión exista en ms-catalogos y esté disponible.
     * @param request DTO con tramoId y camionId
     * @return DTO con información de la asignación creada
     */
    public AsignacionCamionResponse asignarCamionATramo(AsignacionCamionRequest request) {
        // 1. Validar que el tramo exista
        Tramo tramo = tramoRepository.findById(request.tramoId())
                .orElseThrow(() -> new EntityNotFoundException("Tramo no encontrado con ID: " + request.tramoId()));

        // 2. Validar que el tramo no tenga asignación previa
        asignacionRepository.findByTramoId(request.tramoId())
                .ifPresent(a -> {
                    throw new IllegalStateException("El tramo ya tiene un camión asignado. Asignación ID: " + a.getId());
                });

        // 3. Validar que el tramo esté en estado PENDIENTE (no se puede asignar si ya está en curso o completado)
        if (tramo.getEstado() != EstadoTramo.PENDIENTE) {
            throw new IllegalStateException("El tramo debe estar en estado PENDIENTE para asignar un camión. Estado actual: " + tramo.getEstado());
        }

        // 4. Validar capacidad del camión vs contenedor
        if (request.contenedorPesoKg() != null || request.contenedorVolumenM3() != null) {
            validarCapacidadCamion(request.camionId(), request.contenedorPesoKg(), request.contenedorVolumenM3());
        }

        // 5. TODO: En un sistema real, validar que el camión exista en ms-catalogos mediante RestTemplate o Feign
        // Ejemplo: camionClient.existeCamion(request.camionId()) o lanzar EntityNotFoundException

        // 6. Crear la asignación
        AsignacionCamion asignacion = AsignacionCamion.builder()
                .tramo(tramo)
                .camionId(request.camionId())
                .build();

        AsignacionCamion asignacionGuardada = asignacionRepository.save(asignacion);

        // 7. Mapear a DTO de respuesta
        return new AsignacionCamionResponse(
                asignacionGuardada.getId(),
                tramo.getId(),
                tramo.getOrden(),
                tramo.getOrigenNombre(),
                tramo.getDestinoNombre(),
                tramo.getDistanciaKmPlan(),
                tramo.getDuracionMinPlan(),
                tramo.getEstado(),
                asignacionGuardada.getCamionId(),
                asignacionGuardada.getFechaAsignacion(),
                asignacionGuardada.getConfirmado()
        );
    }

    /**
     * Valida que el camión tenga capacidad suficiente para el contenedor.
     * En sistema real, consultaría ms-catalogos vía REST para obtener los datos del camión.
     * Por ahora, simula con datos conocidos de prueba.
     * 
     * @param camionId UUID del camión
     * @param contenedorPesoKg Peso del contenedor en kg
     * @param contenedorVolumenM3 Volumen del contenedor en m3
     * @throws IllegalStateException si el camión no tiene capacidad suficiente
     */
    private void validarCapacidadCamion(UUID camionId, java.math.BigDecimal contenedorPesoKg, java.math.BigDecimal contenedorVolumenM3) {
        // TODO: En sistema real, hacer llamada REST a ms-catalogos:
        // String url = "http://ms-catalogos:8081/camiones/" + camionId;
        // Camion camion = restTemplate.getForObject(url, Camion.class);
        
        // Simulación con datos conocidos de prueba
        CamionCapacidad camion = obtenerCapacidadCamion(camionId);
        
        // Validar peso
        if (contenedorPesoKg != null && camion.capacidadKg() != null) {
            if (contenedorPesoKg.compareTo(camion.capacidadKg()) > 0) {
                throw new IllegalStateException(String.format(
                    "El contenedor (%.2f kg) supera la capacidad de peso del camión %s (%.2f kg)",
                    contenedorPesoKg, camion.patente(), camion.capacidadKg()
                ));
            }
        }
        
        // Validar volumen
        if (contenedorVolumenM3 != null && camion.volumenM3() != null) {
            if (contenedorVolumenM3.compareTo(camion.volumenM3()) > 0) {
                throw new IllegalStateException(String.format(
                    "El contenedor (%.2f m³) supera la capacidad de volumen del camión %s (%.2f m³)",
                    contenedorVolumenM3, camion.patente(), camion.volumenM3()
                ));
            }
        }
    }

    /**
     * Obtiene la capacidad de un camión (simulado).
     * En sistema real, consultaría ms-catalogos.
     */
    private CamionCapacidad obtenerCapacidadCamion(UUID camionId) {
        // Camión 1: b8f1b9c5-1c22-4b89-9a75-0193f1a0e111 (AA123BB)
        if (camionId.toString().equals("b8f1b9c5-1c22-4b89-9a75-0193f1a0e111")) {
            return new CamionCapacidad(
                    "AA123BB",
                    new java.math.BigDecimal("25000.0"),  // 25 toneladas
                    new java.math.BigDecimal("60.0")       // 60 m³
            );
        }
        
        // Camión 2: c6e2d7f0-8b4c-4c3a-9b1b-6b2f5e2d2f22 (CC456DD)
        if (camionId.toString().equals("c6e2d7f0-8b4c-4c3a-9b1b-6b2f5e2d2f22")) {
            return new CamionCapacidad(
                    "CC456DD",
                    new java.math.BigDecimal("18000.0"),  // 18 toneladas
                    new java.math.BigDecimal("45.0")       // 45 m³
            );
        }
        
        // Camión desconocido - usar capacidad mínima estándar
        return new CamionCapacidad(
                camionId.toString().substring(0, 8),
                new java.math.BigDecimal("10000.0"),  // 10 toneladas por defecto
                new java.math.BigDecimal("30.0")       // 30 m³ por defecto
        );
    }

    /**
     * Record auxiliar para encapsular la capacidad del camión.
     */
    private record CamionCapacidad(
            String patente,
            java.math.BigDecimal capacidadKg,
            java.math.BigDecimal volumenM3
    ) {}
}