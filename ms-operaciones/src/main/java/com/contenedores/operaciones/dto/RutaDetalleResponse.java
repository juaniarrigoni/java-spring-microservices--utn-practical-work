package com.contenedores.operaciones.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO para respuesta detallada de ruta con todos sus tramos.
 * Incluye tiempo total estimado y costo estimado.
 */
public record RutaDetalleResponse(
        UUID id,
        UUID solicitudId,
        BigDecimal distanciaKmTotal,
        Integer duracionMinTotal,
        BigDecimal costoEstimadoTotal,
        LocalDateTime fechaPlanificacion,
        List<TramoDetalle> tramos
) {
    public record TramoDetalle(
            UUID id,
            Integer orden,
            String origenNombre,
            BigDecimal origenLat,
            BigDecimal origenLng,
            String destinoNombre,
            BigDecimal destinoLat,
            BigDecimal destinoLng,
            BigDecimal distanciaKmPlan,
            Integer duracionMinPlan,
            String estado,
            UUID camionAsignadoId,
            Boolean camionConfirmado
    ) {}
}
