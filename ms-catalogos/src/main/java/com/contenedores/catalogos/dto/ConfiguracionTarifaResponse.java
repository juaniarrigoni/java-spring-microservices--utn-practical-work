package com.contenedores.catalogos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ConfiguracionTarifaResponse(
    UUID id,
    String nombre,
    String descripcion,
    BigDecimal precioLitroCombustible,
    BigDecimal cargoGestionPorTramo,
    BigDecimal velocidadPromedioKmH,
    BigDecimal costoEstadiaDiarioDefault,
    Boolean activa,
    LocalDateTime vigenciaDesde,
    LocalDateTime vigenciaHasta,
    LocalDateTime fechaCreacion,
    LocalDateTime fechaModificacion
) {}
