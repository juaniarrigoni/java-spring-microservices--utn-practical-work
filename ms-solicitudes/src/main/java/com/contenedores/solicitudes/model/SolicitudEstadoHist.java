package com.contenedores.solicitudes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "solicitud_estado_hist")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudEstadoHist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id", nullable = false)
    private Solicitud solicitud;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estado;

    @Column(nullable = false)
    private LocalDateTime fechaHora;
    private String observacion;
    private UUID actorId; // ID del usuario/sistema que realizó el cambio de estado

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (fechaHora == null) {
            fechaHora = LocalDateTime.now();
        }
    }
}