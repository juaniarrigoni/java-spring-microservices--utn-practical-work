package com.contenedores.catalogos.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "camiones")
public class Camion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 16)
    private String patente;

    @Column(name = "capacidad_kg", nullable = false, precision = 12, scale = 2)
    private BigDecimal capacidadKg;

    @Column(name = "volumen_m3", nullable = false, precision = 12, scale = 2)
    private BigDecimal volumenM3;

    @Column(nullable = false, length = 32)
    private String tipo;

    @Column(nullable = false)
    private boolean activo = true;

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
