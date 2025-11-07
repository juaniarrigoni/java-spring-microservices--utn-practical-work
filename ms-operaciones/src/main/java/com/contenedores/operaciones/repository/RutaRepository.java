package com.contenedores.operaciones.repository;

import com.contenedores.operaciones.model.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, UUID> {
    // No se necesitan métodos personalizados por ahora
}