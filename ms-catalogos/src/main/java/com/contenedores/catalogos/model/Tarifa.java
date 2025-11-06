package com.contenedores.catalogos.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "TARIFA")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Tarifa {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "UUID")
    UUID id;

    @Column(nullable = false)
    String nombre;

    @Column(nullable = false)
    Double precioBase;

    @Column(nullable = false)
    Double precioKm;

    @Column(nullable = false)
    Double precioKg;

    @Column(nullable = false)
    Double precioM3;

    @Column(nullable = false)
    LocalDate vigenciaDesde;

    LocalDate vigenciaHasta;

    @Column(nullable = false)
    Boolean activa = true;
}
