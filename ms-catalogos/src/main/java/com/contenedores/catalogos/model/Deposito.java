package com.contenedores.catalogos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Entity @Table(name = "depositos")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Deposito {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    private String nombre;
    private String direccion;
    
    @Column(name = "latitud")
    private BigDecimal latitud;
    
    @Column(name = "longitud")
    private BigDecimal longitud;
    
    private Boolean activo;
    
    @Column(name = "costo_estadia_diario")
    private BigDecimal costoEstadiaDiario;

    @PrePersist
    protected void onCreate() {
        if (id == null) { id = UUID.randomUUID(); }
        if (activo == null) { activo = true; }
    }
}