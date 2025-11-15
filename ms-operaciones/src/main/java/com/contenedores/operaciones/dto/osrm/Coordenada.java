package com.contenedores.operaciones.dto.osrm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa una coordenada geográfica (latitud, longitud).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coordenada {
    private Double latitud;
    private Double longitud;

    /**
     * Convierte la coordenada al formato requerido por OSRM: "longitud,latitud"
     * IMPORTANTE: OSRM usa el orden [longitud, latitud]
     */
    public String toOsrmFormat() {
        return String.format("%.6f,%.6f", longitud, latitud);
    }
}
