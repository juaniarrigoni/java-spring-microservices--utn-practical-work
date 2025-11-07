package com.contenedores.operaciones.service;

import com.contenedores.operaciones.model.Ruta;
import com.contenedores.operaciones.repository.RutaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RutaService {
    private final RutaRepository rutaRepository;

    public RutaService(RutaRepository rutaRepository) {
        this.rutaRepository = rutaRepository;
    }

    public Ruta create(Ruta ruta) {
        // Aquí iría la lógica de negocio para crear una ruta,
        // como calcular distancias con la API de Google, etc.
        return rutaRepository.save(ruta);
    }

    public List<Ruta> findAll() {
        return rutaRepository.findAll();
    }
}