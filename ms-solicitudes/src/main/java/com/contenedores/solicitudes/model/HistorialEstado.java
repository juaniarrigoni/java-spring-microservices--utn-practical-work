package com.contenedores.solicitudes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad para registrar el historial de cambios de estado de una solicitud.
 * Permite el seguimiento cronológico de los estados del envío.
 */
@Entity
@Table(name = "historial_estados")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialEstado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id", nullable = false)
    private Solicitud solicitud;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estadoAnterior;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estadoNuevo;
    
    @Column(nullable = false)
    private LocalDateTime fechaCambio;
    
    private String observaciones;
    
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (fechaCambio == null) {
            fechaCambio = LocalDateTime.now();
        }
    }
}
