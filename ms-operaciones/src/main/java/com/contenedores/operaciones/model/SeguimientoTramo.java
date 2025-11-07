package com.contenedores.operaciones.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity @Table(name = "seguimiento_tramos")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SeguimientoTramo {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tramo_id", nullable = false)
    private Tramo tramo;
    @Column(nullable = false)
    private LocalDateTime timestamp; // CORREGIDO: de 'ts' a 'timestamp'
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEventoSeguimiento evento;
    private BigDecimal latitud; // CORREGIDO: de 'lat' a 'latitud'
    private BigDecimal longitud; // CORREGIDO: de 'lng' a 'longitud'
    private String notas;

    @PrePersist
    protected void onCreate() {
        if (id == null) { id = UUID.randomUUID(); }
        if (timestamp == null) { timestamp = LocalDateTime.now(); }
    }
}