package com.contenedores.operaciones.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record RutaRequest(
        UUID solicitudId,
        BigDecimal distanciaKmPlan,
        Integer duracionMinPlan,
        List<TramoRequest> tramos
) {}
