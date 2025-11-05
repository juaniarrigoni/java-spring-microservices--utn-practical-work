package com.contenedores.operaciones.dto;

import jakarta.validation.constraints.NotBlank;

public record PlanificacionRequest(
        @NotBlank(message = "El origen es obligatorio") String origen,
        @NotBlank(message = "El destino es obligatorio") String destino,
        int paradasIntermedias) {
}
