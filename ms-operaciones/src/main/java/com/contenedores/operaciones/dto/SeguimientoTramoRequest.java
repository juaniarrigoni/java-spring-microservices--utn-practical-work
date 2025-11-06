package com.contenedores.operaciones.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record SeguimientoTramoRequest(
        UUID tramoId,
        String evento,
        BigDecimal lat,
        BigDecimal lng,
        String notas
) {}
