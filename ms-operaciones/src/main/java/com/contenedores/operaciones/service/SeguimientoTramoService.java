package com.contenedores.operaciones.service;

import com.contenedores.operaciones.model.SeguimientoTramo;
import com.contenedores.operaciones.repository.SeguimientoTramoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SeguimientoTramoService {

    private final SeguimientoTramoRepository seguimientoRepository;

    public SeguimientoTramo registrarEvento(SeguimientoTramo evento) {
        evento.setTs(LocalDateTime.now());
        return seguimientoRepository.save(evento);
    }

    public List<SeguimientoTramo> obtenerPorTramo(UUID tramoId) {
        return seguimientoRepository.findByTramoIdOrderByTsAsc(tramoId);
    }
}
