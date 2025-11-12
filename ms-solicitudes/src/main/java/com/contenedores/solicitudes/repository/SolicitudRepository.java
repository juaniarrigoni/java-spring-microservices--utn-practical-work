package com.contenedores.solicitudes.repository;

import com.contenedores.solicitudes.model.EstadoSolicitud;
import com.contenedores.solicitudes.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, UUID>, JpaSpecificationExecutor<Solicitud> {
    List<Solicitud> findByClienteId(UUID clienteId);
    List<Solicitud> findByContenedorId(UUID contenedorId);
    List<Solicitud> findByEstadoActualIn(List<EstadoSolicitud> estados);
    
    /**
     * Busca solicitudes con cliente y contenedor cargados eagerly para evitar LazyInitializationException.
     * Útil para consultas que requieren toda la información.
     */
    @Query("SELECT s FROM Solicitud s " +
           "LEFT JOIN FETCH s.cliente " +
           "LEFT JOIN FETCH s.contenedor " +
           "WHERE s.estadoActual IN :estados")
    List<Solicitud> findByEstadoActualInWithDetails(@Param("estados") List<EstadoSolicitud> estados);
}