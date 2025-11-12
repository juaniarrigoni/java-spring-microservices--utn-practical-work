package com.contenedores.operaciones.dto;

import java.util.UUID;

/**
 * DTO para asignar un camión a un tramo de traslado.
 * El camión debe existir en ms-catalogos y estar disponible.
 * Opcionalmente valida que el contenedor no supere la capacidad del camión.
 */
public record AsignacionCamionRequest(
        UUID tramoId,
        UUID camionId,
        // Campos opcionales para validar capacidad del contenedor
        java.math.BigDecimal contenedorPesoKg,
        java.math.BigDecimal contenedorVolumenM3
) {
}
