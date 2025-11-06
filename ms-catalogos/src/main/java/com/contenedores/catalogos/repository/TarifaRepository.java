package com.contenedores.catalogos.repository;

import com.contenedores.catalogos.model.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TarifaRepository extends JpaRepository<Tarifa, UUID> { 
    List<Tarifa> findByActivaTrueAndVigenciaDesdeBeforeAndVigenciaHastaAfter(LocalDate vigenciaHasta, LocalDate vigenciaDesde);

}
