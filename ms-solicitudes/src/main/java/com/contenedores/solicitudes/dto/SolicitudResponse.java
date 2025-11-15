package com.contenedores.solicitudes.dto;

import java.time.Instant;

public record SolicitudResponse(long id, String solicitante, String descripcion, String estado, Instant timestamp) {
}
