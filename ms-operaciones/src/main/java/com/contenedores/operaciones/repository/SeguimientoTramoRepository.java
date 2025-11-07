package com.contenedores.operaciones.repository;

import com.contenedores.operaciones.model.SeguimientoTramo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface SeguimientoTramoRepository extends JpaRepository<SeguimientoTramo, UUID> {
    // CORREGIDO: el nombre del método ahora usa 'Timestamp'
    List<SeguimientoTramo> findByTramoIdOrderByTimestampAsc(UUID tramoId);
}