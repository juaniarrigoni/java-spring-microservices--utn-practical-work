package com.contenedores.catalogos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.contenedores.catalogos.dto.DepositoRequest;
import com.contenedores.catalogos.dto.DepositoResponse;
import com.contenedores.catalogos.exception.BadRequestException;
import com.contenedores.catalogos.exception.ConflictException;
import com.contenedores.catalogos.exception.ResourceNotFoundException;
import com.contenedores.catalogos.model.Deposito;
import com.contenedores.catalogos.repository.DepositoRepository;

@Service
@Transactional
public class DepositoService {

    private final DepositoRepository depositoRepository;

    public DepositoService(DepositoRepository depositoRepository) {
        this.depositoRepository = depositoRepository;
    }

    public DepositoResponse create(DepositoRequest request) {
        var nombre = request.getNombre().trim();
        if (nombre.isEmpty()) {
            throw new BadRequestException("El nombre del depósito es obligatorio");
        }
        if (depositoRepository.existsByNombreIgnoreCase(nombre)) {
            throw new ConflictException("Ya existe un depósito con el nombre " + nombre);
        }
        Deposito deposito = new Deposito();
        deposito.setNombre(nombre);
        deposito.setDireccion(request.getDireccion());
        deposito.setLat(request.getLat());
        deposito.setLng(request.getLng());
        deposito.setCostoEstadiaDiario(request.getCostoEstadiaDiario());
        deposito.setActivo(true);
        return toResponse(depositoRepository.save(deposito));
    }

    public DepositoResponse update(Long id, DepositoRequest request) {
        Deposito deposito = findDeposito(id);
        var nombre = request.getNombre().trim();
        if (nombre.isEmpty()) {
            throw new BadRequestException("El nombre del depósito es obligatorio");
        }
        depositoRepository.findByNombreIgnoreCase(nombre)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ConflictException("Ya existe un depósito con el nombre " + nombre);
                });
        deposito.setNombre(nombre);
        deposito.setDireccion(request.getDireccion());
        deposito.setLat(request.getLat());
        deposito.setLng(request.getLng());
        deposito.setCostoEstadiaDiario(request.getCostoEstadiaDiario());
        return toResponse(depositoRepository.save(deposito));
    }

    public List<DepositoResponse> list() {
        return depositoRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public DepositoResponse get(Long id) {
        return toResponse(findDeposito(id));
    }

    public void delete(Long id) {
        depositoRepository.delete(findDeposito(id));
    }

    public DepositoResponse activate(Long id) {
        Deposito deposito = findDeposito(id);
        deposito.setActivo(true);
        return toResponse(depositoRepository.save(deposito));
    }

    public DepositoResponse deactivate(Long id) {
        Deposito deposito = findDeposito(id);
        deposito.setActivo(false);
        return toResponse(depositoRepository.save(deposito));
    }

    private Deposito findDeposito(Long id) {
        return depositoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el depósito " + id));
    }

    private DepositoResponse toResponse(Deposito deposito) {
        return new DepositoResponse(deposito.getId(), deposito.getNombre(), deposito.getDireccion(), deposito.getLat(),
                deposito.getLng(), deposito.getCostoEstadiaDiario(), deposito.isActivo());
    }
}
