package com.contenedores.solicitudes.repository;

import com.contenedores.solicitudes.model.HistorialEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HistorialEstadoRepository extends JpaRepository<HistorialEstado, UUID> {
    
    /**
     * Obtiene el historial de estados de una solicitud ordenado cronológicamente.
     */
    @Query("SELECT h FROM HistorialEstado h WHERE h.solicitud.id = :solicitudId ORDER BY h.fechaCambio ASC")
    List<HistorialEstado> findBySolicitudIdOrderByFechaCambioAsc(@Param("solicitudId") UUID solicitudId);
}
