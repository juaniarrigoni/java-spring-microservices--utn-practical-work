package com.contenedores.operaciones.repository;

import com.contenedores.operaciones.model.AsignacionCamion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AsignacionCamionRepository extends JpaRepository<AsignacionCamion, UUID> {

    // Buscar la asignación actual de un camión
    Optional<AsignacionCamion> findByCamionId(UUID camionId);
}
