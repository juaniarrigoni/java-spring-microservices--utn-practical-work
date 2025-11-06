package com.contenedores.operaciones.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "RUTAS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false)
    UUID solicitudId; // referencia externa (ms-solicitudes)

    BigDecimal distanciaKmPlan;
    Integer duracionMinPlan;
    LocalDateTime fechaPlan;

    @OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Tramo> tramos = new ArrayList<>();
}
