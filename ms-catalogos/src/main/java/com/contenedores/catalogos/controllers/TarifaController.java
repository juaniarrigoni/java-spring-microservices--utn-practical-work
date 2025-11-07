package com.contenedores.catalogos.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.contenedores.catalogos.dto.TarifaRequest;
import com.contenedores.catalogos.dto.TarifaResponse;
import com.contenedores.catalogos.service.TarifaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tarifas")
public class TarifaController {

    private final TarifaService tarifaService;

    public TarifaController(TarifaService tarifaService) {
        this.tarifaService = tarifaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TarifaResponse create(@Valid @RequestBody TarifaRequest request) {
        return tarifaService.create(request);
    }

    @GetMapping
    public List<TarifaResponse> list() {
        return tarifaService.list();
    }

    @GetMapping("/{id}")
    public TarifaResponse get(@PathVariable Long id) {
        return tarifaService.get(id);
    }

    @PutMapping("/{id}")
    public TarifaResponse update(@PathVariable Long id, @Valid @RequestBody TarifaRequest request) {
        return tarifaService.update(id, request);
    }

    @PatchMapping("/{id}/activar")
    public TarifaResponse activate(@PathVariable Long id) {
        return tarifaService.activate(id);
    }

    @PatchMapping("/{id}/desactivar")
    public TarifaResponse deactivate(@PathVariable Long id) {
        return tarifaService.deactivate(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        tarifaService.delete(id);
    }
}
