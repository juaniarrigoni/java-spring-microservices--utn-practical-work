package com.contenedores.catalogos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ConfiguracionTarifaRequest(
    @NotBlank(message = "El nombre es obligatorio")
    String nombre,
    
    String descripcion,
    
    @NotNull(message = "El precio del litro de combustible es obligatorio")
    @Positive(message = "El precio del litro de combustible debe ser positivo")
    BigDecimal precioLitroCombustible,
    
    @NotNull(message = "El cargo de gestión por tramo es obligatorio")
    @Positive(message = "El cargo de gestión por tramo debe ser positivo")
    BigDecimal cargoGestionPorTramo,
    
    @NotNull(message = "La velocidad promedio es obligatoria")
    @Positive(message = "La velocidad promedio debe ser positiva")
    BigDecimal velocidadPromedioKmH,
    
    @NotNull(message = "El costo de estadía diario es obligatorio")
    @Positive(message = "El costo de estadía diario debe ser positivo")
    BigDecimal costoEstadiaDiarioDefault,
    
    Boolean activa,
    
    LocalDateTime vigenciaDesde,
    
    LocalDateTime vigenciaHasta
) {}
