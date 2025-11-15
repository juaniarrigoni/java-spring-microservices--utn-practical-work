package com.contenedores.operaciones.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO para solicitar el cálculo de una tarifa aproximada antes de crear la ruta.
 * Permite estimar costos basándose en valores promedio de camiones elegibles.
 */
public record TarifaAproximadaRequest(
        UUID solicitudId,
        BigDecimal distanciaKmEstimada,
        Integer cantidadTramos,
        BigDecimal contenedorPesoKg,
        BigDecimal contenedorVolumenM3
) {
}
