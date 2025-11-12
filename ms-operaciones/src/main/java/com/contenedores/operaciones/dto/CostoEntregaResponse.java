package com.contenedores.operaciones.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * DTO para el cálculo detallado del costo total de entrega (REQ-8 REFINADO).
 * Incluye desglose por componente según especificaciones:
 * - Cargo de gestión: valor fijo por cantidad de tramos
 * - Costo de traslado: suma de (distancia × costo_base_km del camión asignado)
 * - Costo de combustible: suma de (distancia × consumo_camión × precio_litro)
 * - Costo de estadías: suma de (días × costo_diario del depósito)
 */
public record CostoEntregaResponse(
        UUID rutaId,
        UUID solicitudId,
        // Componentes del costo REAL según especificación
        BigDecimal costoTraslado,       // suma de costos de traslado por tramo (distancia × costo_base_km_camion)
        BigDecimal costoCombustible,    // suma de costos de combustible por tramo (distancia × consumo × precio_litro)
        BigDecimal costoEstadias,       // suma de estadías en depósitos (días × costo_diario)
        BigDecimal cargoGestion,        // valor fijo por cantidad de tramos (cantidad × cargo_por_tramo)
        BigDecimal costoTotal,          // suma de todos los componentes
        // Detalles de cálculo
        BigDecimal distanciaKmTotal,
        BigDecimal pesoKg,
        BigDecimal volumenM3,
        BigDecimal precioLitroCombustible,
        List<TramoDetalle> tramos,
        List<EstadiaDetalle> estadias,
        // Información de configuración
        String observaciones
) {
    /**
     * Detalle de costo por tramo.
     */
    public record TramoDetalle(
            Integer orden,
            String origen,
            String destino,
            BigDecimal distanciaKm,
            String camionPatente,
            BigDecimal costoBaseKmCamion,
            BigDecimal consumoCombustibleKm,
            BigDecimal costoTraslado,
            BigDecimal costoCombustible,
            BigDecimal costoTotalTramo
    ) {
    }
    
    /**
     * Detalle de una estadía en depósito.
     */
    public record EstadiaDetalle(
            Integer ordenTramo,
            String depositoNombre,
            String fechaEntrada,
            String fechaSalida,
            BigDecimal diasEstadia,
            BigDecimal costoDiario,
            BigDecimal costoTotal
    ) {
    }
}
