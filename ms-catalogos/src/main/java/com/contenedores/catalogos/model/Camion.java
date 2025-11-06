package com.contenedores.catalogos.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

@Entity
@Table(name = "CAMION")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Camion {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "UUID")
    UUID id;

    @Column(nullable = false, unique = true)
    String patente;

    @Column(nullable = false)
    Double capacidadKg;

    @Column(nullable = false)
    Double volumenM3;

    @Column(nullable = false)
    String tipo; // Sider / Chasis / Tractor

    @Column(nullable = false)
    Boolean activo = true;
}
