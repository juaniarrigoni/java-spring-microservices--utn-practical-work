package com.contenedores.operaciones.repository;

import com.contenedores.operaciones.model.Tramo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface TramoRepository extends JpaRepository<Tramo, UUID> {
    // MÉTODO AÑADIDO: para buscar tramos por ruta
    List<Tramo> findByRutaIdOrderByOrdenAsc(UUID rutaId);
}