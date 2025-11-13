package com.contenedores.operaciones.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO para la respuesta de tarifa aproximada.
 * Calcula el costo estimado basándose en valores promedio de camiones elegibles.
 */
public record TarifaAproximadaResponse(
        UUID solicitudId,
        BigDecimal distanciaKmEstimada,
        Integer cantidadTramos,
        // Valores promedio de camiones elegibles
        BigDecimal costoBaseKmPromedio,
        BigDecimal consumoCombustiblePromedio,
        Integer cantidadCamionesElegibles,
        // Componentes del costo estimado
        BigDecimal cargoGestionEstimado,
        BigDecimal costoTrasladoEstimado,
        BigDecimal costoCombustibleEstimado,
        BigDecimal costoTotalEstimado,
        // Información adicional
        BigDecimal precioLitroCombustible,
        String observaciones
) {
}
