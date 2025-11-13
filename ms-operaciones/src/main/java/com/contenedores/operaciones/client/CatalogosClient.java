package com.contenedores.operaciones.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

/**
 * Cliente REST para consumir servicios de ms-catalogos
 */
@Component
@Slf4j
public class CatalogosClient {

    private final RestTemplate restTemplate;
    private final String catalogosUrl;

    public CatalogosClient(
            RestTemplate restTemplate,
            @Value("${ms-catalogos.url}") String catalogosUrl) {
        this.restTemplate = restTemplate;
        this.catalogosUrl = catalogosUrl;
    }

    /**
     * DTO interno para la configuración de tarifas
     */
    public record ConfiguracionTarifaDTO(
        BigDecimal precioLitroCombustible,
        BigDecimal cargoGestionPorTramo,
        BigDecimal velocidadPromedioKmH,
        BigDecimal costoEstadiaDiarioDefault
    ) {}

    /**
     * Obtiene la configuración de tarifas activa desde ms-catalogos
     */
    public ConfiguracionTarifaDTO obtenerConfiguracionActiva() {
        try {
            String url = catalogosUrl + "/configuracion-tarifas/activa";
            log.debug("Consultando configuración de tarifas en: {}", url);
            
            return restTemplate.getForObject(url, ConfiguracionTarifaDTO.class);
        } catch (Exception e) {
            log.error("Error al obtener configuración de tarifas desde ms-catalogos: {}", 
                e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene el costo base por km para un volumen específico
     */
    public BigDecimal obtenerCostoBaseKmPorVolumen(BigDecimal volumenM3) {
        try {
            String url = catalogosUrl + "/tarifas-volumen/costo-base?volumenM3=" + volumenM3;
            log.debug("Consultando costo base para volumen {} en: {}", volumenM3, url);
            
            return restTemplate.getForObject(url, BigDecimal.class);
        } catch (Exception e) {
            log.error("Error al obtener costo base por volumen desde ms-catalogos: {}", 
                e.getMessage());
            throw e;
        }
    }
}
