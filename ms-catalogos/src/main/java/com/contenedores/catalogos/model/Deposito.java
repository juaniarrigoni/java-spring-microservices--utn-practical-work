package com.contenedores.catalogos.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

@Entity
@Table(name = "DEPOSITO")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Deposito {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "UUID")
    UUID id;

    @Column(nullable = false)
    String nombre;

    @Column(nullable = false)
    String direccion;

    @Column(nullable = false)
    Double lat;

    @Column(nullable = false)
    Double lng;

    @Column(nullable = false)
    Boolean activo = true;
}
