package com.contenedores.operaciones.repository;

import com.contenedores.operaciones.model.AsignacionCamion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AsignacionCamionRepository extends JpaRepository<AsignacionCamion, UUID> {
    // MÉTODO AÑADIDO: para buscar asignaciones por camión
    List<AsignacionCamion> findByCamionId(UUID camionId);
    
    // MÉTODO AÑADIDO: para buscar asignación por tramo
    Optional<AsignacionCamion> findByTramoId(UUID tramoId);
}