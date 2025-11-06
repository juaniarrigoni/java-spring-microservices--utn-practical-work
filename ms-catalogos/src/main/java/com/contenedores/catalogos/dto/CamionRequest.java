package com.contenedores.catalogos.dto;

import lombok.Data;

@Data
public class CamionRequest {
    private String patente;
    private Double capacidadKg;
    private Double volumenM3;
    private String tipo;
}
