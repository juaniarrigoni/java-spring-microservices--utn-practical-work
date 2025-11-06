package com.contenedores.operaciones.dto;

import java.util.UUID;

public record AsignarCamionRequest(
        UUID tramoId,
        UUID camionId
) {}
