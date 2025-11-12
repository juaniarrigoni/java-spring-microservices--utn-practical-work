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

    /**
     * Obtiene un depósito por su ID.
     * @param id El UUID del depósito.
     * @return La entidad Deposito encontrada.
     */
    public Deposito findById(java.util.UUID id) {
        return depositoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Depósito no encontrado con ID: " + id));
    }

    /**
     * Actualiza un depósito existente.
     * @param id El UUID del depósito a actualizar.
     * @param depositoActualizado Los datos actualizados del depósito.
     * @return La entidad Deposito actualizada.
     */
    public Deposito update(java.util.UUID id, Deposito depositoActualizado) {
        Deposito depositoExistente = findById(id);
        
        // Validaciones de coordenadas
        if (depositoActualizado.getLatitud().compareTo(new BigDecimal("-90")) < 0 || 
                depositoActualizado.getLatitud().compareTo(new BigDecimal("90")) > 0) {
            throw new IllegalArgumentException("La latitud debe estar entre -90 y 90.");
        }
        if (depositoActualizado.getLongitud().compareTo(new BigDecimal("-180")) < 0 || 
                depositoActualizado.getLongitud().compareTo(new BigDecimal("180")) > 0) {
            throw new IllegalArgumentException("La longitud debe estar entre -180 y 180.");
        }
        
        // Actualizar campos
        depositoExistente.setNombre(depositoActualizado.getNombre());
        depositoExistente.setDireccion(depositoActualizado.getDireccion());
        depositoExistente.setLatitud(depositoActualizado.getLatitud());
        depositoExistente.setLongitud(depositoActualizado.getLongitud());
        depositoExistente.setCostoEstadiaDiario(depositoActualizado.getCostoEstadiaDiario());
        
        return depositoRepository.save(depositoExistente);
    }

    /**
     * Elimina un depósito.
     * @param id El UUID del depósito a eliminar.
     */
    public void delete(java.util.UUID id) {
        Deposito deposito = findById(id);
        depositoRepository.delete(deposito);
    }
}