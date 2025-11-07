package com.contenedores.operaciones.service;

import com.contenedores.operaciones.model.SeguimientoTramo;
import com.contenedores.operaciones.repository.SeguimientoTramoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SeguimientoTramoService {
    private final SeguimientoTramoRepository seguimientoRepository;

    public SeguimientoTramoService(SeguimientoTramoRepository seguimientoRepository) {
        this.seguimientoRepository = seguimientoRepository;
    }

    public SeguimientoTramo registrarEvento(SeguimientoTramo evento) {
        // CORREGIDO: el setter correcto es setTimestamp
        if (evento.getTimestamp() == null) {
            evento.setTimestamp(LocalDateTime.now());
        }
        return seguimientoRepository.save(evento);
    }

    public List<SeguimientoTramo> getHistorialPorTramo(UUID tramoId) {
        // CORREGIDO: el método del repo ahora se llama findByTramoIdOrderByTimestampAsc
        return seguimientoRepository.findByTramoIdOrderByTimestampAsc(tramoId);
    }
}