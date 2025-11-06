package com.contenedores.catalogos.repository;

import com.contenedores.catalogos.model.Deposito;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DepositoRepository extends JpaRepository<Deposito, UUID> { }
