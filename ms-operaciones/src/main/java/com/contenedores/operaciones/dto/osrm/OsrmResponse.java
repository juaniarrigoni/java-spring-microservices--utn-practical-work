package com.contenedores.operaciones.dto.osrm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

/**
 * Respuesta de la API de OSRM para el endpoint /route
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OsrmResponse {
    private String code;
    private List<Route> routes;
    private List<Waypoint> waypoints;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Route {
        private Double distance; // en metros
        private Double duration; // en segundos
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Waypoint {
        private String name;
        private List<Double> location; // [longitud, latitud]
    }

    public boolean isOk() {
        return "Ok".equalsIgnoreCase(code);
    }

    public Double getDistanciaKm() {
        if (routes != null && !routes.isEmpty()) {
            return routes.get(0).getDistance() / 1000.0;
        }
        return null;
    }

    public Double getDuracionMinutos() {
        if (routes != null && !routes.isEmpty()) {
            return routes.get(0).getDuration() / 60.0;
        }
        return null;
    }
}
