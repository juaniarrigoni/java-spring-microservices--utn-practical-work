package com.contenedores.solicitudes.dto;

import com.contenedores.solicitudes.model.EstadoSolicitud;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para mostrar el historial de cambios de estado de una solicitud.
 * Representa el seguimiento cronológico del envío.
 */
public record HistorialEstadoResponse(
        UUID id,
        EstadoSolicitud estadoAnterior,
        EstadoSolicitud estadoNuevo,
        LocalDateTime fechaCambio,
        String observaciones
) {
}
