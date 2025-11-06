package com.contenedores.operaciones.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "SEGUIMIENTOS_TRAMO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeguimientoTramo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne
    @JoinColumn(name = "TRAMO_ID")
    Tramo tramo;

    LocalDateTime ts;
    String evento; // INICIO, ARRIBO_ORIGEN, SALIDA_ORIGEN, ARRIBO_DESTINO, FIN, INCIDENTE
    BigDecimal lat;
    BigDecimal lng;
    String notas;
}
