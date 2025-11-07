package com.contenedores.catalogos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TarifaRequest {

    @NotBlank
    private String nombre;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal precioBase;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal precioKm;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal precioKg;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal precioM3;

    @NotNull
    private LocalDate vigenciaDesde;

    @NotNull
    private LocalDate vigenciaHasta;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(BigDecimal precioBase) {
        this.precioBase = precioBase;
    }

    public BigDecimal getPrecioKm() {
        return precioKm;
    }

    public void setPrecioKm(BigDecimal precioKm) {
        this.precioKm = precioKm;
    }

    public BigDecimal getPrecioKg() {
        return precioKg;
    }

    public void setPrecioKg(BigDecimal precioKg) {
        this.precioKg = precioKg;
    }

    public BigDecimal getPrecioM3() {
        return precioM3;
    }

    public void setPrecioM3(BigDecimal precioM3) {
        this.precioM3 = precioM3;
    }

    public LocalDate getVigenciaDesde() {
        return vigenciaDesde;
    }

    public void setVigenciaDesde(LocalDate vigenciaDesde) {
        this.vigenciaDesde = vigenciaDesde;
    }

    public LocalDate getVigenciaHasta() {
        return vigenciaHasta;
    }

    public void setVigenciaHasta(LocalDate vigenciaHasta) {
        this.vigenciaHasta = vigenciaHasta;
    }
}
