package com.contenedores.catalogos.dto;

import java.time.Instant;

public record ServiceInfo(String service, String version, Instant timestamp) {
}
