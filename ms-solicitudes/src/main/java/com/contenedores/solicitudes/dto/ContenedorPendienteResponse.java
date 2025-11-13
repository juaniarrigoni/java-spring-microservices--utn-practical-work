package com.contenedores.solicitudes.dto;

import com.contenedores.solicitudes.model.EstadoSolicitud;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para consultar contenedores pendientes de entrega con su ubicación y estado.
 * Incluye información consolidada de Solicitud, Contenedor y Cliente.
 */
public record ContenedorPendienteResponse(
        UUID solicitudId,
        UUID contenedorId,
        String codigoContenedor,
        String tipoContenedor,
        BigDecimal pesoKg,
        BigDecimal volumenM3,
        EstadoSolicitud estadoActual,
        LocalDateTime fechaCreacion,
        LocalDateTime etaEstimado,
        String clienteRazonSocial,
        String clienteCuit,
        String origenNombre,
        BigDecimal origenLat,
        BigDecimal origenLng,
        String destinoNombre,
        BigDecimal destinoLat,
        BigDecimal destinoLng,
        Integer distanciaKmEstimada,
        BigDecimal costoEstimado,
        LocalDateTime tiempoRealEntrega,
        String ubicacionActual // Descripción de la ubicación actual basada en el estado
) {
}
