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
