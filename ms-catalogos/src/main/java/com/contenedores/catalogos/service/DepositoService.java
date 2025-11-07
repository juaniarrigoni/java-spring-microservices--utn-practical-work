package com.contenedores.catalogos.service;

import com.contenedores.catalogos.model.Deposito;
import com.contenedores.catalogos.repository.DepositoRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class DepositoService {

    private final DepositoRepository depositoRepository;

    public DepositoService(DepositoRepository depositoRepository) {
        this.depositoRepository = depositoRepository;
    }

    /**
     * Crea un nuevo depósito.
     * @param deposito La entidad Deposito a guardar.
     * @return La entidad Deposito guardada.
     */
    public Deposito create(Deposito deposito) {
        // Validaciones de coordenadas
        if (deposito.getLatitud().compareTo(new BigDecimal("-90")) < 0 || deposito.getLatitud().compareTo(new BigDecimal("90")) > 0) {
            throw new IllegalArgumentException("La latitud debe estar entre -90 y 90.");
        }
        if (deposito.getLongitud().compareTo(new BigDecimal("-180")) < 0 || deposito.getLongitud().compareTo(new BigDecimal("180")) > 0) {
            throw new IllegalArgumentException("La longitud debe estar entre -180 y 180.");
        }
        return depositoRepository.save(deposito);
    }

    /**
     * Obtiene una lista de todos los depósitos registrados.
     * @return Una lista de entidades Deposito.
     */
    public List<Deposito> findAll() {
        return depositoRepository.findAll();
    }
}