package com.contenedores.catalogos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity @Table(name = "tarifas")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Tarifa {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    private String nombre;
    
    @Column(name = "precio_base")
    private BigDecimal precioBase;
    
    @Column(name = "precio_km")
    private BigDecimal precioKm;
    
    @Column(name = "precio_kg")
    private BigDecimal precioKg;
    
    @Column(name = "precio_m3")
    private BigDecimal precioM3;
    
    @Column(name = "vigencia_desde")
    private LocalDate vigenciaDesde;
    
    @Column(name = "vigencia_hasta")
    private LocalDate vigenciaHasta;
    
    private Boolean activa;

    @PrePersist
    protected void onCreate() {
        if (id == null) { id = UUID.randomUUID(); }
        if (activa == null) { activa = true; }
    }
}