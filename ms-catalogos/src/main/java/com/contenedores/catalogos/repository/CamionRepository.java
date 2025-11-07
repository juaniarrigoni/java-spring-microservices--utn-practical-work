package com.contenedores.catalogos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.contenedores.catalogos.model.Camion;

public interface CamionRepository extends JpaRepository<Camion, Long> {

    boolean existsByPatenteIgnoreCase(String patente);

    Optional<Camion> findByPatenteIgnoreCase(String patente);
}
