package com.contenedores.catalogos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TarifaPorVolumenResponse(
    UUID id,
    String nombre,
    String descripcion,
    BigDecimal volumenMinM3,
    BigDecimal volumenMaxM3,
    BigDecimal costoBaseKm,
    Boolean activa,
    Integer ordenPrioridad,
    LocalDateTime vigenciaDesde,
    LocalDateTime vigenciaHasta,
    LocalDateTime fechaCreacion,
    LocalDateTime fechaModificacion
) {}
