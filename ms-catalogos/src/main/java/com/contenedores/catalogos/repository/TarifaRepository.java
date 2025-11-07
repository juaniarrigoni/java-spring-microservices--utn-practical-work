package com.contenedores.catalogos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.contenedores.catalogos.model.Tarifa;

public interface TarifaRepository extends JpaRepository<Tarifa, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    Optional<Tarifa> findByNombreIgnoreCase(String nombre);
}
