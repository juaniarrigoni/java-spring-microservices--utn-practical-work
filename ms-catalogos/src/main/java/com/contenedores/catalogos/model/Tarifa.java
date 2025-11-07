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
    private BigDecimal precioBase;
    private BigDecimal precioKm;
    private BigDecimal precioKg;
    private BigDecimal precioM3;
    private LocalDate vigenciaDesde;
    private LocalDate vigenciaHasta;
    private Boolean activa;

    @PrePersist
    protected void onCreate() {
        if (id == null) { id = UUID.randomUUID(); }
        if (activa == null) { activa = true; }
    }
}