package com.contenedores.solicitudes.repository;

import com.contenedores.solicitudes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    Optional<Cliente> findByCuit(String cuit);
    Optional<Cliente> findByEmail(String email);
}