package com.contenedores.operaciones.dto;

import com.contenedores.operaciones.model.EstadoTramo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para la respuesta al iniciar o finalizar un tramo.
 * Incluye información del estado del tramo y las fechas de inicio/fin reales.
 */
public record TramoEstadoResponse(
        UUID tramoId,
        Integer orden,
        String origenNombre,
        String destinoNombre,
        BigDecimal distanciaKmPlan,
        Integer duracionMinPlan,
        EstadoTramo estado,
        LocalDateTime fechaInicioReal,
        LocalDateTime fechaFinReal,
        UUID camionAsignadoId,
        Boolean camionConfirmado
) {
}
