package com.contenedores.solicitudes.service;

import com.contenedores.solicitudes.model.Cliente;
import com.contenedores.solicitudes.model.EstadoSolicitud;
import com.contenedores.solicitudes.model.Solicitud;
import com.contenedores.solicitudes.repository.ClienteRepository;
import com.contenedores.solicitudes.repository.SolicitudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final ClienteRepository clienteRepository;

    public SolicitudService(SolicitudRepository solicitudRepository, ClienteRepository clienteRepository) {
        this.solicitudRepository = solicitudRepository;
        this.clienteRepository = clienteRepository;
    }

    /**
     * Crea una nueva solicitud en el sistema.
     * @param solicitud La entidad Solicitud a guardar.
     * @return La entidad Solicitud guardada con su ID asignado.
     */
    @Transactional
    public Solicitud create(Solicitud solicitud) {
        // Validamos que el cliente exista
        if (solicitud.getCliente() != null && solicitud.getCliente().getId() != null) {
            Cliente cliente = clienteRepository.findById(solicitud.getCliente().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + solicitud.getCliente().getId()));
            solicitud.setCliente(cliente);
        } else {
            throw new IllegalArgumentException("Se requiere un cliente válido para crear la solicitud");
        }

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
     * Obtiene todas las solicitudes pendientes (CREADA, VALIDADA, PLANIFICADA, EN_CURSO).
     * @return Una lista de solicitudes pendientes.
     */
    public List<Solicitud> findPendientes() {
        List<EstadoSolicitud> estadosPendientes = Arrays.asList(
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
}
