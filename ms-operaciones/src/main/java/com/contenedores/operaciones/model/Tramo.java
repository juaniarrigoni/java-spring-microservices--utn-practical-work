package com.contenedores.operaciones.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity @Table(name = "tramos")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Tramo {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ruta_id", nullable = false)
    private Ruta ruta;
    @Column(nullable = false)
    private Integer orden;
    private String origenNombre;
    private BigDecimal origenLat;
    private BigDecimal origenLng;
    private String destinoNombre;
    private BigDecimal destinoLat;
    private BigDecimal destinoLng;
    private BigDecimal distanciaKmPlan;
    private Integer duracionMinPlan;
    @Enumerated(EnumType.STRING)
    private EstadoTramo estado;
    private LocalDateTime fechaInicioReal;
    private LocalDateTime fechaFinReal;
    @OneToOne(mappedBy = "tramo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AsignacionCamion asignacionCamion;

    @PrePersist
    protected void onCreate() {
        if (id == null) { id = UUID.randomUUID(); }
        if (estado == null) { estado = EstadoTramo.PENDIENTE; }
    }
}