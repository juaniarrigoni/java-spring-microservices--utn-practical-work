package com.contenedores.catalogos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TarifaPorVolumenRequest(
    @NotBlank(message = "El nombre es obligatorio")
    String nombre,
    
    String descripcion,
    
    @NotNull(message = "El volumen mínimo es obligatorio")
    BigDecimal volumenMinM3,
    
    BigDecimal volumenMaxM3, // null = sin límite superior
    
    @NotNull(message = "El costo base por km es obligatorio")
    @Positive(message = "El costo base por km debe ser positivo")
    BigDecimal costoBaseKm,
    
    Boolean activa,
    
    Integer ordenPrioridad,
    
    LocalDateTime vigenciaDesde,
    
    LocalDateTime vigenciaHasta
) {}
