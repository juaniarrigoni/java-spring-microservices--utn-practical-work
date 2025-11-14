package com.contenedores.operaciones.dto.osrm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response con información de distancia calculada entre dos puntos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DistanciaResponse {
    private String origenNombre;
    private String destinoNombre;
    private Coordenada origen;
    private Coordenada destino;
    private Double distanciaKm;
    private Double duracionMinutos;
    private Double duracionHoras;
    private Boolean exitoso;
    private String mensaje;

    public static DistanciaResponse success(String origenNombre, String destinoNombre,
                                           Coordenada origen, Coordenada destino,
                                           Double distanciaKm, Double duracionMinutos) {
        return DistanciaResponse.builder()
                .origenNombre(origenNombre)
                .destinoNombre(destinoNombre)
                .origen(origen)
                .destino(destino)
                .distanciaKm(distanciaKm)
                .duracionMinutos(duracionMinutos)
                .duracionHoras(duracionMinutos / 60.0)
                .exitoso(true)
                .mensaje("Distancia calculada exitosamente")
                .build();
    }

    public static DistanciaResponse error(String mensaje) {
        return DistanciaResponse.builder()
                .exitoso(false)
                .mensaje(mensaje)
                .build();
    }
}
