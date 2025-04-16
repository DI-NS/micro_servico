package com.medicamento.medicamento_microservico.Controller;

import com.medicamento.medicamento_microservico.Model.Medicamento;
import com.medicamento.medicamento_microservico.Service.MedicamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/medicamento")
public class MedicamentoController {

    @Autowired
    private MedicamentoService medicamentoService;

    @PostMapping
    public Medicamento createMedicamento(@RequestBody Medicamento medicamento) {
        return medicamentoService.create(medicamento);
    }

    @GetMapping
    public List<Medicamento> getAllMedicamento() {
        return medicamentoService.findAll();
    }

    @GetMapping("/{id}")
    public Medicamento getMedicamentoById(@PathVariable Long id) {
        return medicamentoService.findById(id);
    }

    @PutMapping("/{id}")
    public Medicamento updateMedicamento(@PathVariable Long id, @RequestBody Medicamento medicamento) {
        return medicamentoService.update(id, medicamento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicamento(@PathVariable Long id) {
        medicamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
