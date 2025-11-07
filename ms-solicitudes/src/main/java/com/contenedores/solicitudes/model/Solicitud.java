package com.contenedores.solicitudes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "solicitudes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estadoActual; // CREADA/VALIDADA/PLANIFICADA/EN_CURSO/COMPLETADA/CANCELADA

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // Un contenedor por solicitud
    @JoinColumn(name = "contenedor_id", nullable = false)
    private Contenedor contenedor;

    private UUID tarifaId; // "ref a ms-catalogos"
    private BigDecimal costoEstimado;
    private Integer distanciaKmEstimada;
    private LocalDateTime etaEstimado; // Estimated Time of Arrival

    private String origenNombre;
    private BigDecimal origenLat;
    private BigDecimal origenLng;
    private String destinoNombre;
    private BigDecimal destinoLat;
    private BigDecimal destinoLng;

    private BigDecimal costoReal; // Calculado al finalizar
    private LocalDateTime tiempoRealEntrega; // Calculado al finalizar

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (estadoActual == null) {
            estadoActual = EstadoSolicitud.CREADA;
        }
    }
}