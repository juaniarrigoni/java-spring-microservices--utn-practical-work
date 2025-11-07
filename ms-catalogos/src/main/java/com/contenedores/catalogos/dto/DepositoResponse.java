package com.contenedores.catalogos.dto;

import java.math.BigDecimal;

public class DepositoResponse {

    private Long id;
    private String nombre;
    private String direccion;
    private BigDecimal lat;
    private BigDecimal lng;
    private BigDecimal costoEstadiaDiario;
    private boolean activo;

    public DepositoResponse() {
    }

    public DepositoResponse(Long id, String nombre, String direccion, BigDecimal lat, BigDecimal lng,
            BigDecimal costoEstadiaDiario, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.lat = lat;
        this.lng = lng;
        this.costoEstadiaDiario = costoEstadiaDiario;
        this.activo = activo;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public BigDecimal getCostoEstadiaDiario() {
        return costoEstadiaDiario;
    }

    public void setCostoEstadiaDiario(BigDecimal costoEstadiaDiario) {
        this.costoEstadiaDiario = costoEstadiaDiario;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
