package com.contenedores.catalogos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TarifaResponse {

    private Long id;
    private String nombre;
    private BigDecimal precioBase;
    private BigDecimal precioKm;
    private BigDecimal precioKg;
    private BigDecimal precioM3;
    private LocalDate vigenciaDesde;
    private LocalDate vigenciaHasta;
    private boolean activa;

    public TarifaResponse() {
    }

    public TarifaResponse(Long id, String nombre, BigDecimal precioBase, BigDecimal precioKm, BigDecimal precioKg,
            BigDecimal precioM3, LocalDate vigenciaDesde, LocalDate vigenciaHasta, boolean activa) {
        this.id = id;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.precioKm = precioKm;
        this.precioKg = precioKg;
        this.precioM3 = precioM3;
        this.vigenciaDesde = vigenciaDesde;
        this.vigenciaHasta = vigenciaHasta;
        this.activa = activa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
