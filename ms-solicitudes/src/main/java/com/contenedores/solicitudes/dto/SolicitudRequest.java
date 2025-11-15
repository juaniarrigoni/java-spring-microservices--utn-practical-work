package com.contenedores.solicitudes.dto;

import jakarta.validation.constraints.NotBlank;

public record SolicitudRequest(
        @NotBlank(message = "El solicitante es obligatorio") String solicitante,
        @NotBlank(message = "La descripción es obligatoria") String descripcion) {
}
