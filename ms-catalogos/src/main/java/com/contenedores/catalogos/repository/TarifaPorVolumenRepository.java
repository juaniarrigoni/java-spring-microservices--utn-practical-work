package com.contenedores.catalogos.repository;

import com.contenedores.catalogos.model.TarifaPorVolumen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TarifaPorVolumenRepository extends JpaRepository<TarifaPorVolumen, UUID> {

    /**
     * Encuentra todas las tarifas activas ordenadas por prioridad
     */
    List<TarifaPorVolumen> findByActivaTrueOrderByOrdenPrioridadAsc();

    /**
     * Encuentra la tarifa aplicable para un volumen específico
     */
    @Query("SELECT t FROM TarifaPorVolumen t WHERE t.activa = true " +
           "AND t.volumenMinM3 <= :volumen " +
           "AND (t.volumenMaxM3 IS NULL OR t.volumenMaxM3 > :volumen) " +
           "ORDER BY t.ordenPrioridad ASC")
    List<TarifaPorVolumen> findByVolumenContenedor(@Param("volumen") BigDecimal volumen);

    /**
     * Encuentra la tarifa aplicable para un volumen en una fecha específica
     */
    @Query("SELECT t FROM TarifaPorVolumen t WHERE t.activa = true " +
           "AND t.volumenMinM3 <= :volumen " +
           "AND (t.volumenMaxM3 IS NULL OR t.volumenMaxM3 > :volumen) " +
           "AND t.vigenciaDesde <= :fecha " +
           "AND (t.vigenciaHasta IS NULL OR t.vigenciaHasta >= :fecha) " +
           "ORDER BY t.ordenPrioridad ASC")
    List<TarifaPorVolumen> findByVolumenYFecha(
        @Param("volumen") BigDecimal volumen,
        @Param("fecha") LocalDateTime fecha
    );

    /**
     * Encuentra tarifas que se solapan con un rango dado
     */
    @Query("SELECT t FROM TarifaPorVolumen t WHERE t.activa = true " +
           "AND ((t.volumenMinM3 >= :min AND t.volumenMinM3 < :max) " +
           "OR (t.volumenMaxM3 > :min AND t.volumenMaxM3 <= :max) " +
           "OR (t.volumenMinM3 <= :min AND (t.volumenMaxM3 IS NULL OR t.volumenMaxM3 >= :max)))")
    List<TarifaPorVolumen> findSolapamientoRango(
        @Param("min") BigDecimal volumenMin,
        @Param("max") BigDecimal volumenMax
    );
}
