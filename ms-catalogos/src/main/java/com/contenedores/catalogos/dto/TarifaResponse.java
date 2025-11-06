package com.contenedores.catalogos.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TarifaResponse {
    private UUID id;
    private String nombre;
    private Double precioBase;
    private Double precioKm;
    private Double precioKg;
    private Double precioM3;
    private LocalDate vigenciaDesde;
    private LocalDate vigenciaHasta;
    private Boolean activa;
}
