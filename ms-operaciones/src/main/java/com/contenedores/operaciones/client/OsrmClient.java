package com.contenedores.operaciones.client;

import com.contenedores.operaciones.dto.osrm.Coordenada;
import com.contenedores.operaciones.dto.osrm.OsrmResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Cliente para consumir la API de OSRM (Open Source Routing Machine)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OsrmClient {

    private final RestTemplate restTemplate;

    @Value("${osrm.url:http://localhost:5000}")
    private String osrmUrl;

    /**
     * Calcula la ruta entre dos coordenadas usando OSRM
     * 
     * @param origen Coordenada de origen
     * @param destino Coordenada de destino
     * @return OsrmResponse con distancia y duración
     */
    public OsrmResponse calcularRuta(Coordenada origen, Coordenada destino) {
        try {
            String url = construirUrl(origen, destino);
            log.debug("Consultando OSRM: {}", url);
            
            OsrmResponse response = restTemplate.getForObject(url, OsrmResponse.class);
            
            if (response != null && response.isOk()) {
                log.info("Ruta calculada: {} km, {} min", 
                        response.getDistanciaKm(), 
                        response.getDuracionMinutos());
                return response;
            } else {
                log.warn("OSRM devolvió código no exitoso: {}", 
                        response != null ? response.getCode() : "null");
                return null;
            }
        } catch (Exception e) {
            log.error("Error al consultar OSRM: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Construye la URL para la petición a OSRM
     * Formato: {baseUrl}/route/v1/driving/{coordinates}?overview=false
     */
    private String construirUrl(Coordenada origen, Coordenada destino) {
        // OSRM requiere formato: longitud,latitud;longitud,latitud
        String coordinates = String.format("%s;%s", 
                origen.toOsrmFormat(), 
                destino.toOsrmFormat());

        return String.format("%s/route/v1/driving/%s?overview=false", 
                osrmUrl, coordinates);
    }
}
