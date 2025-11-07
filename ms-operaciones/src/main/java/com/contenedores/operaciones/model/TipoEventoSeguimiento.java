package com.contenedores.operaciones.model;

public enum TipoEventoSeguimiento {
    INICIO,
    ARRIBO_ORIGEN,
    SALIDA_ORIGEN,
    ARRIBO_DEPOSITO, // Si el tramo es a un depósito
    SALIDA_DEPOSITO, // Si el tramo sale de un depósito
    ARRIBO_DESTINO,
    FIN,
    INCIDENTE // Cualquier evento inesperado
}