package com.contenedores.solicitudes.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para registrar la finalización de una solicitud con datos reales.
 */
public record RegistrarFinalizacionRequest(
        BigDecimal costoReal,
        LocalDateTime tiempoRealEntrega
) {
}
