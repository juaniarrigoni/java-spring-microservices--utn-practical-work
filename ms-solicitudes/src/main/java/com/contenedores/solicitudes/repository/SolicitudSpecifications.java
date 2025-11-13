package com.contenedores.solicitudes.repository;

import com.contenedores.solicitudes.model.EstadoSolicitud;
import com.contenedores.solicitudes.model.Solicitud;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Especificaciones JPA para construir queries dinámicas con filtros opcionales.
 */
public class SolicitudSpecifications {

    /**
     * Construye una Specification dinámica basada en los filtros proporcionados.
     * Todos los filtros son opcionales (AND lógico).
     */
    public static Specification<Solicitud> withFilters(
            List<EstadoSolicitud> estados,
            UUID clienteId,
            String clienteCuit,
            String codigoContenedor,
            LocalDateTime fechaDesde,
            LocalDateTime fechaHasta
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por estados (IN)
            if (estados != null && !estados.isEmpty()) {
                predicates.add(root.get("estadoActual").in(estados));
            }

            // Filtro por cliente ID
            if (clienteId != null) {
                predicates.add(criteriaBuilder.equal(root.get("cliente").get("id"), clienteId));
            }

            // Filtro por cliente CUIT
            if (clienteCuit != null && !clienteCuit.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("cliente").get("cuit"), clienteCuit));
            }

            // Filtro por código de contenedor
            if (codigoContenedor != null && !codigoContenedor.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("contenedor").get("codigo"), codigoContenedor));
            }

            // Filtro por rango de fechas
            if (fechaDesde != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("fechaCreacion"), fechaDesde));
            }
            if (fechaHasta != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("fechaCreacion"), fechaHasta));
            }

            // JOIN FETCH para evitar LazyInitializationException
            if (query != null) {
                query.distinct(true);
                root.fetch("cliente");
                root.fetch("contenedor");
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
