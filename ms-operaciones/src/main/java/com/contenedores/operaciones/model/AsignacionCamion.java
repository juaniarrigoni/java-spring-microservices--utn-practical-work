package com.contenedores.operaciones.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity @Table(name = "asignaciones_camiones")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AsignacionCamion {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tramo_id", nullable = false, unique = true)
    private Tramo tramo;
    private UUID camionId;
    private LocalDateTime fechaAsignacion;
    private Boolean confirmado;

    @PrePersist
    protected void onCreate() {
        if (id == null) { id = UUID.randomUUID(); }
        if (fechaAsignacion == null) { fechaAsignacion = LocalDateTime.now(); }
        if (confirmado == null) { confirmado = false; }
    }
}