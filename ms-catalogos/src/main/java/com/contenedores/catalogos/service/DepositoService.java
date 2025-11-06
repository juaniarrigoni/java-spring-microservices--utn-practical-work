package com.contenedores.catalogos.service;

import com.contenedores.catalogos.dto.DepositoResponse;
import com.contenedores.catalogos.model.Deposito;
import com.contenedores.catalogos.repository.DepositoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepositoService {

    private final DepositoRepository depositoRepository;

    public DepositoService(DepositoRepository depositoRepository) {
        this.depositoRepository = depositoRepository;
    }

    public List<DepositoResponse> findAll() {
        return depositoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public DepositoResponse create(Deposito request) {
        validarDeposito(request);
        Deposito saved = depositoRepository.save(request);
        return toResponse(saved);
    }

    // Reglas de negocio
    private void validarDeposito(Deposito request) {
        if (request.getNombre() == null || request.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del depósito es obligatorio.");
        }

        if (request.getDireccion() == null || request.getDireccion().isBlank()) {
            throw new IllegalArgumentException("La dirección del depósito es obligatoria.");
        }

        if (request.getLat() == null || request.getLat() < -90 || request.getLat() > 90) {
            throw new IllegalArgumentException("La latitud debe estar entre -90 y 90 grados.");
        }

        if (request.getLng() == null || request.getLng() < -180 || request.getLng() > 180) {
            throw new IllegalArgumentException("La longitud debe estar entre -180 y 180 grados.");
        }

        // ⚠️ Regla del enunciado: "Cada depósito debe mantener un costo de estadía diario"
        if (request.getCostoEstadiaDiario() == null || request.getCostoEstadiaDiario() <= 0) {
            throw new IllegalArgumentException("El costo de estadía diario debe ser mayor que cero.");
        }
    }

    private DepositoResponse toResponse(Deposito d) {
        return DepositoResponse.builder()
                .id(d.getId())
                .nombre(d.getNombre())
                .direccion(d.getDireccion())
                .lat(d.getLat())
                .lng(d.getLng())
                .activo(d.getActivo())
                .build();
    }
}
