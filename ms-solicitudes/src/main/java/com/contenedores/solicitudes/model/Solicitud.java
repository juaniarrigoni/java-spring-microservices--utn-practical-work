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

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_actual", nullable = false)
    private EstadoSolicitud estadoActual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "contenedor_id", nullable = false)
    private Contenedor contenedor;

    @Column(name = "tarifa_id")
    private UUID tarifaId;
    
    @Column(name = "costo_estimado")
    private BigDecimal costoEstimado;
    
    @Column(name = "distancia_km_estimada")
    private Integer distanciaKmEstimada;
    
    @Column(name = "eta_estimado")
    private LocalDateTime etaEstimado;

    @Column(name = "origen_nombre")
    private String origenNombre;
    
    @Column(name = "origen_lat")
    private BigDecimal origenLat;
    
    @Column(name = "origen_lng")
    private BigDecimal origenLng;
    
    @Column(name = "destino_nombre")
    private String destinoNombre;
    
    @Column(name = "destino_lat")
    private BigDecimal destinoLat;
    
    @Column(name = "destino_lng")
    private BigDecimal destinoLng;

    @Column(name = "costo_real")
    private BigDecimal costoReal;
    
    @Column(name = "tiempo_real_entrega")
    private LocalDateTime tiempoRealEntrega;

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