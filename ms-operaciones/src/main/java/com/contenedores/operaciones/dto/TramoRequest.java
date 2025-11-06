package com.contenedores.operaciones.dto;

import java.math.BigDecimal;

public record TramoRequest(
        Integer orden,
        String origenNombre,
        BigDecimal origenLat,
        BigDecimal origenLng,
        String destinoNombre,
        BigDecimal destinoLat,
        BigDecimal destinoLng,
        BigDecimal distanciaKmPlan,
        Integer duracionMinPlan
) {}
