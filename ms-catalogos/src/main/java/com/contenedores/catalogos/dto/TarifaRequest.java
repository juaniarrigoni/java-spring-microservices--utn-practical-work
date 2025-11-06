package com.contenedores.catalogos.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TarifaRequest {
    private String nombre;
    private Double precioBase;
    private Double precioKm;
    private Double precioKg;
    private Double precioM3;
    private LocalDate vigenciaDesde;
    private LocalDate vigenciaHasta;
}
