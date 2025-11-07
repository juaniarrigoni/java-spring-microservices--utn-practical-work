package com.contenedores.catalogos.repository;

import com.contenedores.catalogos.model.Deposito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface DepositoRepository extends JpaRepository<Deposito, UUID> {
    // No se necesitan métodos personalizados por ahora
}