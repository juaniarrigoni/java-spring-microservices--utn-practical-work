package com.contenedores.solicitudes.model;

public enum EstadoSolicitud {
    BORRADOR,      // Estado inicial cuando el cliente crea la solicitud
    PROGRAMADA,    // Solicitud validada y programada para transporte
    EN_TRANSITO,   // Contenedor en tránsito
    ENTREGADA,     // Transporte completado y entregado
    CANCELADA,     // Solicitud cancelada
    
    // Estados heredados para compatibilidad con datos existentes
    CREADA,
    VALIDADA,
    PLANIFICADA,
    EN_CURSO,
    COMPLETADA
}