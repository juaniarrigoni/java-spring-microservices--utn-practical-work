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

import com.contenedores.catalogos.dto.DepositoRequest;
import com.contenedores.catalogos.dto.DepositoResponse;
import com.contenedores.catalogos.service.DepositoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/depositos")
public class DepositoController {

    private final DepositoService depositoService;

    public DepositoController(DepositoService depositoService) {
        this.depositoService = depositoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DepositoResponse create(@Valid @RequestBody DepositoRequest request) {
        return depositoService.create(request);
    }

    @GetMapping
    public List<DepositoResponse> list() {
        return depositoService.list();
    }

    @GetMapping("/{id}")
    public DepositoResponse get(@PathVariable Long id) {
        return depositoService.get(id);
    }

    @PutMapping("/{id}")
    public DepositoResponse update(@PathVariable Long id, @Valid @RequestBody DepositoRequest request) {
        return depositoService.update(id, request);
    }

    @PatchMapping("/{id}/activar")
    public DepositoResponse activate(@PathVariable Long id) {
        return depositoService.activate(id);
    }

    @PatchMapping("/{id}/desactivar")
    public DepositoResponse deactivate(@PathVariable Long id) {
        return depositoService.deactivate(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        depositoService.delete(id);
    }
}
