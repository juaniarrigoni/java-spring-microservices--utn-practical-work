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
    
    @Column(name = "capacidad_kg")
    private BigDecimal capacidadKg;
    
    @Column(name = "volumen_m3")  // AÑADE ESTO
    private BigDecimal volumenM3;
    
    private String tipo;
    
    @Column(name = "consumo_combustible_km")
    private BigDecimal consumoCombustibleKm;
    
    @Column(name = "costo_base_km")
    private BigDecimal costoBaseKm;
    
    @Column(name = "nombre_transportista")
    private String nombreTransportista;
    
    @Column(name = "telefono_transportista")
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