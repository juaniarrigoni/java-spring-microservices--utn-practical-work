package com.contenedores.operaciones.repository;

import com.contenedores.operaciones.model.EstadoTramo;
import com.contenedores.operaciones.model.Tramo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TramoRepository extends JpaRepository<Tramo, UUID> {

    // Buscar todos los tramos de una ruta
    List<Tramo> findByRutaId(UUID rutaId);

    // Buscar tramos por estado (pendientes, en curso, etc.)
    List<Tramo> findByEstado(EstadoTramo estado);
}
