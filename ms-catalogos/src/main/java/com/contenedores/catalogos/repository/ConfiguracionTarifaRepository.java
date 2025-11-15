package com.contenedores.catalogos.repository;

import com.contenedores.catalogos.model.ConfiguracionTarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfiguracionTarifaRepository extends JpaRepository<ConfiguracionTarifa, UUID> {

    /**
     * Encuentra la configuración activa actual
     */
    Optional<ConfiguracionTarifa> findByActivaTrue();

    /**
     * Encuentra todas las configuraciones activas
     */
    List<ConfiguracionTarifa> findAllByActivaTrueOrderByFechaCreacionDesc();

    /**
     * Encuentra la configuración vigente en una fecha específica
     */
    @Query("SELECT c FROM ConfiguracionTarifa c WHERE c.activa = true " +
           "AND c.vigenciaDesde <= :fecha " +
           "AND (c.vigenciaHasta IS NULL OR c.vigenciaHasta >= :fecha)")
    Optional<ConfiguracionTarifa> findVigenteEnFecha(@Param("fecha") LocalDateTime fecha);

    /**
     * Verifica si existe una configuración activa
     */
    boolean existsByActivaTrue();
}
