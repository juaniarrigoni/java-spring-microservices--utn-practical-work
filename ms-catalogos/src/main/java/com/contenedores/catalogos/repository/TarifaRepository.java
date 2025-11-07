package com.contenedores.catalogos.repository;

import com.contenedores.catalogos.model.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, UUID> {
    List<Tarifa> findByActivaTrueAndVigenciaDesdeBeforeAndVigenciaHastaAfter(LocalDate fechaFinRango, LocalDate fechaInicioRango);
}