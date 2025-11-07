package com.contenedores.catalogos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.contenedores.catalogos.model.Deposito;

public interface DepositoRepository extends JpaRepository<Deposito, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    Optional<Deposito> findByNombreIgnoreCase(String nombre);
}
