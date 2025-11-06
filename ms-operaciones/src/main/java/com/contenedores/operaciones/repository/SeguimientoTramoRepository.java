package com.contenedores.operaciones.repository;

import com.contenedores.operaciones.model.SeguimientoTramo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SeguimientoTramoRepository extends JpaRepository<SeguimientoTramo, UUID> {

    // Buscar todos los seguimientos asociados a un tramo
    List<SeguimientoTramo> findByTramoIdOrderByTsAsc(UUID tramoId);
}
