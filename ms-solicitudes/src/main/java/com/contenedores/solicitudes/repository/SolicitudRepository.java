package com.contenedores.solicitudes.repository;

import com.contenedores.solicitudes.model.EstadoSolicitud;
import com.contenedores.solicitudes.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, UUID> {
    List<Solicitud> findByClienteId(UUID clienteId);
    List<Solicitud> findByContenedorId(UUID contenedorId);
    List<Solicitud> findByEstadoActualIn(List<EstadoSolicitud> estados);
}