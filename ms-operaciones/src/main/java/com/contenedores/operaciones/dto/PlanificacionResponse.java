package com.contenedores.operaciones.dto;

import java.time.Instant;

public record PlanificacionResponse(String origen, String destino, int paradasIntermedias,
        String estado, Instant generadoEn, long duracionEstimadaMin) {
}
