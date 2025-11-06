package com.contenedores.operaciones.service;

import com.contenedores.operaciones.model.Ruta;
import com.contenedores.operaciones.repository.RutaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RutaService {

    private final RutaRepository rutaRepository;

    public Ruta crearRuta(Ruta ruta) {
        return rutaRepository.save(ruta);
    }

    public List<Ruta> listarRutas() {
        return rutaRepository.findAll();
    }

    public Ruta obtenerPorId(UUID id) {
        return rutaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada: " + id));
    }
}
