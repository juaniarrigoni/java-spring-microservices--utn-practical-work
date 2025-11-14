package com.contenedores.operaciones.service;

import com.contenedores.operaciones.client.OsrmClient;
import com.contenedores.operaciones.dto.osrm.Coordenada;
import com.contenedores.operaciones.dto.osrm.DistanciaResponse;
import com.contenedores.operaciones.dto.osrm.OsrmResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Servicio para cálculo de distancias entre puntos usando OSRM
 * Casos de uso:
 * - Origen a Depósito
 * - Depósito a Destino  
 * - Depósito a Depósito
 * - Origen a Destino (directo)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DistanciaService {

    private final OsrmClient osrmClient;

    /**
     * Calcula la distancia entre dos puntos
     */
    public DistanciaResponse calcularDistancia(String origenNombre, Coordenada origen,
                                               String destinoNombre, Coordenada destino) {
        log.info("Calculando distancia entre {} y {}", origenNombre, destinoNombre);

        if (!validarCoordenadas(origen) || !validarCoordenadas(destino)) {
            return DistanciaResponse.error("Coordenadas inválidas");
        }

        OsrmResponse osrmResponse = osrmClient.calcularRuta(origen, destino);

        if (osrmResponse == null || !osrmResponse.isOk()) {
            return DistanciaResponse.error("No se pudo calcular la ruta");
        }

        return DistanciaResponse.success(
                origenNombre, destinoNombre,
                origen, destino,
                osrmResponse.getDistanciaKm(),
                osrmResponse.getDuracionMinutos()
        );
    }

    /**
     * Calcula distancia entre origen y depósito
     */
    public DistanciaResponse calcularOrigenADeposito(
            String origenNombre, Coordenada origen,
            String depositoNombre, Coordenada deposito) {
        
        return calcularDistancia(
                origenNombre, origen,
                "Depósito " + depositoNombre, deposito
        );
    }

    /**
     * Calcula distancia entre depósito y destino
     */
    public DistanciaResponse calcularDepositoADestino(
            String depositoNombre, Coordenada deposito,
            String destinoNombre, Coordenada destino) {
        
        return calcularDistancia(
                "Depósito " + depositoNombre, deposito,
                destinoNombre, destino
        );
    }

    /**
     * Calcula distancia entre dos depósitos
     */
    public DistanciaResponse calcularEntreDepositos(
            String deposito1Nombre, Coordenada deposito1,
            String deposito2Nombre, Coordenada deposito2) {
        
        return calcularDistancia(
                "Depósito " + deposito1Nombre, deposito1,
                "Depósito " + deposito2Nombre, deposito2
        );
    }

    /**
     * Calcula distancia directa origen-destino (sin depósito)
     */
    public DistanciaResponse calcularDirecto(
            String origenNombre, Coordenada origen,
            String destinoNombre, Coordenada destino) {
        
        return calcularDistancia(origenNombre, origen, destinoNombre, destino);
    }

    /**
     * Calcula solo la distancia en km (método simplificado)
     */
    public Double calcularDistanciaKm(Coordenada origen, Coordenada destino) {
        OsrmResponse response = osrmClient.calcularRuta(origen, destino);
        return response != null ? response.getDistanciaKm() : null;
    }

    private boolean validarCoordenadas(Coordenada coord) {
        if (coord == null || coord.getLatitud() == null || coord.getLongitud() == null) {
            return false;
        }
        
        return coord.getLatitud() >= -90 && coord.getLatitud() <= 90 &&
               coord.getLongitud() >= -180 && coord.getLongitud() <= 180;
    }
}
