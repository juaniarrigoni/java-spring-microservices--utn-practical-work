package com.contenedores.operaciones.service;

import com.contenedores.operaciones.model.AsignacionCamion;
import com.contenedores.operaciones.repository.AsignacionCamionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AsignacionCamionService {

    private final AsignacionCamionRepository asignacionRepository;

    public Optional<AsignacionCamion> buscarPorCamion(UUID camionId) {
        return asignacionRepository.findByCamionId(camionId);
    }
}
