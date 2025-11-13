package com.contenedores.solicitudes.service;

import com.contenedores.solicitudes.dto.ContenedorPendienteResponse;
import com.contenedores.solicitudes.model.Cliente;
import com.contenedores.solicitudes.model.Contenedor;
import com.contenedores.solicitudes.model.EstadoSolicitud;
import com.contenedores.solicitudes.model.Solicitud;
import com.contenedores.solicitudes.repository.ClienteRepository;
import com.contenedores.solicitudes.repository.ContenedorRepository;
import com.contenedores.solicitudes.repository.SolicitudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final ClienteRepository clienteRepository;
    private final ContenedorRepository contenedorRepository;
    private final com.contenedores.solicitudes.repository.HistorialEstadoRepository historialEstadoRepository;

    public SolicitudService(SolicitudRepository solicitudRepository, 
                           ClienteRepository clienteRepository,
                           ContenedorRepository contenedorRepository,
                           com.contenedores.solicitudes.repository.HistorialEstadoRepository historialEstadoRepository) {
        this.solicitudRepository = solicitudRepository;
        this.clienteRepository = clienteRepository;
        this.contenedorRepository = contenedorRepository;
        this.historialEstadoRepository = historialEstadoRepository;
    }

    /**
     * Crea una nueva solicitud en el sistema.
     * Si el cliente no existe (búsqueda por CUIT), se crea automáticamente.
     * El contenedor se crea automáticamente con la solicitud (cascade).
     * @param solicitud La entidad Solicitud a guardar.
     * @return La entidad Solicitud guardada con su ID asignado.
     */
    @Transactional
    public Solicitud create(Solicitud solicitud) {
        // Validamos que el cliente tenga información básica
        if (solicitud.getCliente() == null) {
            throw new IllegalArgumentException("Se requiere información del cliente para crear la solicitud");
        }

        Cliente clienteInput = solicitud.getCliente();
        Cliente clienteFinal;
        
        // Si el cliente tiene ID, lo buscamos; si no existe, intentamos por CUIT
        if (clienteInput.getId() != null) {
            clienteFinal = clienteRepository.findById(clienteInput.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + clienteInput.getId()));
        } else if (clienteInput.getCuit() != null && !clienteInput.getCuit().isEmpty()) {
            // Buscamos por CUIT. Si no existe, lo creamos
            clienteFinal = clienteRepository.findByCuit(clienteInput.getCuit())
                    .orElseGet(() -> {
                        // Validamos que tenga información mínima para crear
                        if (clienteInput.getRazonSocial() == null || clienteInput.getRazonSocial().isEmpty()) {
                            throw new IllegalArgumentException("Se requiere razón social para registrar un nuevo cliente");
                        }
                        return clienteRepository.save(clienteInput);
                    });
        } else {
            throw new IllegalArgumentException("Se requiere ID o CUIT del cliente");
        }
        
        solicitud.setCliente(clienteFinal);

        // Validamos que el contenedor tenga información básica
        if (solicitud.getContenedor() == null) {
            throw new IllegalArgumentException("Se requiere información del contenedor");
        }

        return solicitudRepository.save(solicitud);
    }

    /**
     * Obtiene una solicitud por su ID.
     * @param id El UUID de la solicitud.
     * @return La entidad Solicitud encontrada.
     */
    public Solicitud findById(UUID id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada con ID: " + id));
    }

    /**
     * Obtiene todas las solicitudes pendientes (no entregadas ni canceladas).
     * Incluye: BORRADOR, PROGRAMADA, EN_TRANSITO y los estados heredados.
     * @return Una lista de solicitudes pendientes.
     */
    public List<Solicitud> findPendientes() {
        List<EstadoSolicitud> estadosPendientes = Arrays.asList(
                EstadoSolicitud.BORRADOR,
                EstadoSolicitud.PROGRAMADA,
                EstadoSolicitud.EN_TRANSITO,
                // Estados heredados para compatibilidad
                EstadoSolicitud.CREADA,
                EstadoSolicitud.VALIDADA,
                EstadoSolicitud.PLANIFICADA,
                EstadoSolicitud.EN_CURSO
        );
        return solicitudRepository.findByEstadoActualIn(estadosPendientes);
    }

    /**
     * Obtiene todas las solicitudes.
     * @return Una lista de todas las solicitudes.
     */
    public List<Solicitud> findAll() {
        return solicitudRepository.findAll();
    }

    /**
     * Obtiene una solicitud por el código del contenedor.
     * Útil para que el cliente consulte el estado usando el código del contenedor.
     * @param codigoContenedor El código único del contenedor.
     * @return La entidad Solicitud asociada al contenedor.
     */
    public Solicitud findByCodigoContenedor(String codigoContenedor) {
        Contenedor contenedor = contenedorRepository.findByCodigo(codigoContenedor)
                .orElseThrow(() -> new IllegalArgumentException("Contenedor no encontrado con código: " + codigoContenedor));
        
        List<Solicitud> solicitudes = solicitudRepository.findByContenedorId(contenedor.getId());
        
        if (solicitudes.isEmpty()) {
            throw new IllegalArgumentException("No se encontró solicitud para el contenedor: " + codigoContenedor);
        }
        
        // Como la relación es OneToOne, debería haber solo una solicitud
        return solicitudes.get(0);
    }

    /**
     * Busca contenedores pendientes de entrega con filtros opcionales.
     * Por defecto, busca solicitudes que NO estén ENTREGADA ni CANCELADA.
     * @param estados Lista de estados a filtrar (opcional)
     * @param clienteId UUID del cliente (opcional)
     * @param clienteCuit CUIT del cliente (opcional)
     * @param codigoContenedor Código del contenedor (opcional)
     * @param fechaDesde Fecha inicio del rango (opcional)
     * @param fechaHasta Fecha fin del rango (opcional)
     * @return Lista de solicitudes que cumplen los filtros
     */
    public List<Solicitud> findContenedoresPendientes(
            List<EstadoSolicitud> estados,
            UUID clienteId,
            String clienteCuit,
            String codigoContenedor,
            java.time.LocalDateTime fechaDesde,
            java.time.LocalDateTime fechaHasta
    ) {
        // Si no se especifican estados, usar todos los pendientes (no entregados ni cancelados)
        if (estados == null || estados.isEmpty()) {
            estados = Arrays.asList(
                    EstadoSolicitud.BORRADOR,
                    EstadoSolicitud.PROGRAMADA,
                    EstadoSolicitud.EN_TRANSITO,
                    EstadoSolicitud.CREADA,
                    EstadoSolicitud.VALIDADA,
                    EstadoSolicitud.PLANIFICADA,
                    EstadoSolicitud.EN_CURSO
            );
        }

        return solicitudRepository.findAll(
                com.contenedores.solicitudes.repository.SolicitudSpecifications.withFilters(
                        estados,
                        clienteId,
                        clienteCuit,
                        codigoContenedor,
                        fechaDesde,
                        fechaHasta
                )
        );
    }

    /**
     * Convierte una lista de Solicitudes a ContenedorPendienteResponse.
     * Incluye información de ubicación basada en el estado actual.
     */
    public List<ContenedorPendienteResponse> mapToContenedorPendienteResponse(List<Solicitud> solicitudes) {
        return solicitudes.stream()
                .map(this::mapSolicitudToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Mapea una Solicitud individual a ContenedorPendienteResponse.
     * Calcula la ubicación actual basándose en el estado.
     */
    private ContenedorPendienteResponse mapSolicitudToResponse(Solicitud solicitud) {
        String ubicacionActual = determinarUbicacionActual(solicitud);

        return new ContenedorPendienteResponse(
                solicitud.getId(),
                solicitud.getContenedor().getId(),
                solicitud.getContenedor().getCodigo(),
                solicitud.getContenedor().getTipo(),
                solicitud.getContenedor().getPesoKg(),
                solicitud.getContenedor().getVolumenM3(),
                solicitud.getEstadoActual(),
                solicitud.getFechaCreacion(),
                solicitud.getEtaEstimado(),
                solicitud.getCliente().getRazonSocial(),
                solicitud.getCliente().getCuit(),
                solicitud.getOrigenNombre(),
                solicitud.getOrigenLat(),
                solicitud.getOrigenLng(),
                solicitud.getDestinoNombre(),
                solicitud.getDestinoLat(),
                solicitud.getDestinoLng(),
                solicitud.getDistanciaKmEstimada(),
                solicitud.getCostoEstimado(),
                solicitud.getTiempoRealEntrega(),
                ubicacionActual
        );
    }

    /**
     * Determina la ubicación actual del contenedor según el estado de la solicitud.
     * En un sistema real, esto se obtendría de un sistema de tracking con GPS.
     */
    private String determinarUbicacionActual(Solicitud solicitud) {
        return switch (solicitud.getEstadoActual()) {
            case BORRADOR, CREADA, VALIDADA -> "En preparación - Origen: " + solicitud.getOrigenNombre();
            case PROGRAMADA, PLANIFICADA -> "Programado para retiro - Origen: " + solicitud.getOrigenNombre();
            case EN_TRANSITO, EN_CURSO -> "En tránsito hacia " + solicitud.getDestinoNombre();
            case ENTREGADA, COMPLETADA -> "Entregado - Destino: " + solicitud.getDestinoNombre();
            case CANCELADA -> "Solicitud cancelada";
        };
    }

    /**
     * Registra el costo real y tiempo real de entrega al finalizar una solicitud.
     * Actualiza el estado a ENTREGADA/COMPLETADA.
     * 
     * @param solicitudId UUID de la solicitud
     * @param costoReal Costo real calculado de la entrega
     * @param tiempoRealEntrega Fecha/hora real de entrega
     * @return Solicitud actualizada
     */
    @Transactional
    public Solicitud registrarFinalizacion(UUID solicitudId, java.math.BigDecimal costoReal, java.time.LocalDateTime tiempoRealEntrega) {
        Solicitud solicitud = findById(solicitudId);
        
        // Actualizar datos reales
        solicitud.setCostoReal(costoReal);
        solicitud.setTiempoRealEntrega(tiempoRealEntrega);
        
        // Actualizar estado a completada (con historial)
        solicitud.cambiarEstado(EstadoSolicitud.ENTREGADA, "Entrega finalizada. Costo real: " + costoReal);
        
        return solicitudRepository.save(solicitud);
    }
    
    /**
     * Obtiene el historial completo de cambios de estado de una solicitud.
     * El historial se devuelve ordenado cronológicamente.
     * 
     * @param solicitudId UUID de la solicitud
     * @return Lista de registros de historial ordenados por fecha
     */
    @Transactional(readOnly = true)
    public List<com.contenedores.solicitudes.dto.HistorialEstadoResponse> obtenerHistorialEstados(UUID solicitudId) {
        // Verificar que la solicitud existe
        findById(solicitudId);
        
        // Obtener historial ordenado cronológicamente
        return historialEstadoRepository.findBySolicitudIdOrderByFechaCambioAsc(solicitudId)
                .stream()
                .map(h -> new com.contenedores.solicitudes.dto.HistorialEstadoResponse(
                        h.getId(),
                        h.getEstadoAnterior(),
                        h.getEstadoNuevo(),
                        h.getFechaCambio(),
                        h.getObservaciones()
                ))
                .collect(Collectors.toList());
    }
}
