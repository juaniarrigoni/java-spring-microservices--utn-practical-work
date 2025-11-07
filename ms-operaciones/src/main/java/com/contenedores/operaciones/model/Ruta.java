package com.contenedores.operaciones.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity @Table(name = "rutas")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Ruta {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false, unique = true)
    private UUID solicitudId;
    private BigDecimal distanciaKmPlan;
    private Integer duracionMinPlan;
    private LocalDateTime fechaPlan;
    @OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("orden ASC")
    private List<Tramo> tramos;

    @PrePersist
    protected void onCreate() {
        if (id == null) { id = UUID.randomUUID(); }
        if (fechaPlan == null) { fechaPlan = LocalDateTime.now(); }
    }
}