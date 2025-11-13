package com.contenedores.operaciones.dto;

import com.contenedores.operaciones.model.EstadoTramo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para la respuesta de asignación de camión a un tramo.
 * Incluye información del tramo y la asignación realizada.
 */
public record AsignacionCamionResponse(
        UUID asignacionId,
        UUID tramoId,
        Integer ordenTramo,
        String origenNombre,
        String destinoNombre,
        BigDecimal distanciaKmPlan,
        Integer duracionMinPlan,
        EstadoTramo estadoTramo,
        UUID camionId,
        LocalDateTime fechaAsignacion,
        Boolean confirmado
) {
}
