package com.contenedores.catalogos.dto;

import java.math.BigDecimal;

public class CamionResponse {

    private Long id;
    private String patente;
    private BigDecimal capacidadKg;
    private BigDecimal volumenM3;
    private String tipo;
    private boolean activo;

    public CamionResponse() {
    }

    public CamionResponse(Long id, String patente, BigDecimal capacidadKg, BigDecimal volumenM3, String tipo, boolean activo) {
        this.id = id;
        this.patente = patente;
        this.capacidadKg = capacidadKg;
        this.volumenM3 = volumenM3;
        this.tipo = tipo;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public BigDecimal getCapacidadKg() {
        return capacidadKg;
    }

    public void setCapacidadKg(BigDecimal capacidadKg) {
        this.capacidadKg = capacidadKg;
    }

    public BigDecimal getVolumenM3() {
        return volumenM3;
    }

    public void setVolumenM3(BigDecimal volumenM3) {
        this.volumenM3 = volumenM3;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
