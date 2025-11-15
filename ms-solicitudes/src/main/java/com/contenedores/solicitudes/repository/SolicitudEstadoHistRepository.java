package com.contenedores.solicitudes.repository;

import com.contenedores.solicitudes.model.SolicitudEstadoHist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SolicitudEstadoHistRepository extends JpaRepository<SolicitudEstadoHist, UUID> {
    List<SolicitudEstadoHist> findBySolicitudIdOrderByFechaHoraAsc(UUID solicitudId);
}