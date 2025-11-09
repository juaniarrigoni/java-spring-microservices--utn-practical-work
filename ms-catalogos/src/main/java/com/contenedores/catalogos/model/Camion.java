package com.contenedores.catalogos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Entity @Table(name = "camiones")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Camion {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(unique = true, nullable = false)
    private String patente;
    private BigDecimal capacidadKg;
    @Column(name = "volumen_m3")
    private BigDecimal volumenM3;
    private String tipo;
    private BigDecimal consumoCombustibleKm;
    private BigDecimal costoBaseKm;
    private String nombreTransportista;
    private String telefonoTransportista;
    private Boolean activo;
    private Boolean disponible;

    @PrePersist
    protected void onCreate() {
        if (id == null) { id = UUID.randomUUID(); }
        if (activo == null) { activo = true; }
        if (disponible == null) { disponible = true; }
    }
}