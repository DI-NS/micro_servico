package com.medicamento.medicamento_microservico.controller;

import com.medicamento.medicamento_microservico.model.Medicamento;
import com.medicamento.medicamento_microservico.service.MedicamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/medicamento")
public class MedicamentoController {

    private final MedicamentoService service;

    public MedicamentoController(MedicamentoService service) {
        this.service = service;
    }

    @PostMapping
    public Medicamento createMedicamento(@RequestBody Medicamento medicamento,
                                         Authentication auth) {
        Long tokenUbsId = (Long) auth.getCredentials();
        // força o ubsId do token:
        medicamento.setUbsId(tokenUbsId);
        return service.create(medicamento);
    }

    @GetMapping
    public List<Medicamento> getAllMedicamento() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Medicamento getMedicamentoById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public Medicamento updateMedicamento(@PathVariable Long id,
                                         @RequestBody Medicamento medicamento,
                                         Authentication auth) {
        Long tokenUbsId = (Long) auth.getCredentials();
        if (!tokenUbsId.equals(medicamento.getUbsId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Só é permitido atualizar medicamentos da sua UBS");
        }
        // garante coerência
        medicamento.setUbsId(tokenUbsId);
        return service.update(id, medicamento);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMedicamento(@PathVariable Long id,
                                  Authentication auth) {
        // opcional: poderia buscar o medicamento antes e checar o ubsId do token
        service.delete(id);
    }
}
