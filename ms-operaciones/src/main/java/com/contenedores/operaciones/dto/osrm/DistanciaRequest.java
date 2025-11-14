package com.contenedores.operaciones.dto.osrm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request para calcular distancia entre dos puntos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistanciaRequest {
    private String origenNombre;
    private Coordenada origen;
    private String destinoNombre;
    private Coordenada destino;
}
