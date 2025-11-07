package com.contenedores.catalogos.repository;

import com.contenedores.catalogos.model.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface CamionRepository extends JpaRepository<Camion, UUID> {
    boolean existsByPatente(String patente);
}