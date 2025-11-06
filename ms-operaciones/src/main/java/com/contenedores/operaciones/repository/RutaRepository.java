package com.contenedores.operaciones.repository;

import com.contenedores.operaciones.model.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RutaRepository extends JpaRepository<Ruta, UUID> {
}
