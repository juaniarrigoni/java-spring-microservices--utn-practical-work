package com.contenedores.catalogos.dto;

import lombok.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CamionResponse {

    private UUID id;
    private String patente;
    private Double capacidadKg;
    private Double volumenM3;
    private String tipo;
    private Boolean activo;
}
