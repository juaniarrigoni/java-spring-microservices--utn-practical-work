package com.contenedores.catalogos.repository;

import com.contenedores.catalogos.model.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TarifaRepository extends JpaRepository<Tarifa, UUID> { }
