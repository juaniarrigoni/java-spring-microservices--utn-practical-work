package com.contenedores.operaciones.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "TRAMOS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tramo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne
    @JoinColumn(name = "RUTA_ID")
    Ruta ruta;

    Integer orden;

    String origenNombre;
    BigDecimal origenLat;
    BigDecimal origenLng;

    String destinoNombre;
    BigDecimal destinoLat;
    BigDecimal destinoLng;

    BigDecimal distanciaKmPlan;
    Integer duracionMinPlan;

    @Enumerated(EnumType.STRING)
    EstadoTramo estado;

    @OneToOne(mappedBy = "tramo", cascade = CascadeType.ALL)
    AsignacionCamion asignacion;

    @OneToMany(mappedBy = "tramo", cascade = CascadeType.ALL)
    List<SeguimientoTramo> seguimientos = new ArrayList<>();
}
