package com.contenedores.catalogos.dto;

import lombok.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositoResponse {
    private UUID id;
    private String nombre;
    private String direccion;
    private Double lat;
    private Double lng;
    private Boolean activo;
}
