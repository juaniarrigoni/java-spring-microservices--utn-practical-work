package com.contenedores.operaciones.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ASIGNACIONES_CAMION")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AsignacionCamion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @OneToOne
    @JoinColumn(name = "TRAMO_ID")
    Tramo tramo;

    UUID camionId; // referencia al ms-catalogos
    LocalDateTime fechaAsignacion;
    Boolean confirmado;
}
