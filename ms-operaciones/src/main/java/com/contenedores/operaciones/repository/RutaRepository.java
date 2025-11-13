package com.contenedores.operaciones.repository;

import com.contenedores.operaciones.model.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, UUID> {
    
    /**
     * Busca una ruta por el ID de la solicitud asociada.
     * Incluye los tramos con EAGER fetch para evitar lazy loading issues.
     */
    @Query("SELECT r FROM Ruta r LEFT JOIN FETCH r.tramos WHERE r.solicitudId = :solicitudId")
    Optional<Ruta> findBySolicitudIdWithTramos(@Param("solicitudId") UUID solicitudId);
    
    /**
     * Busca una ruta por ID incluyendo sus tramos.
     */
    @Query("SELECT r FROM Ruta r LEFT JOIN FETCH r.tramos WHERE r.id = :id")
    Optional<Ruta> findByIdWithTramos(@Param("id") UUID id);
}