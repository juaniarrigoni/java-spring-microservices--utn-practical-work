package com.contenedores.solicitudes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "contenedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contenedor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String codigo; // "ISO/placa interna"
    private BigDecimal pesoKg;
    private BigDecimal volumenM3;
    private String tipo; // "20/40/40HC, etc."

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}