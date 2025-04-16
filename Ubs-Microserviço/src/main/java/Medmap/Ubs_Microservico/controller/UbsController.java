package Medmap.Ubs_Microservico.controller;

import Medmap.Ubs_Microservico.dto.*;
import Medmap.Ubs_Microservico.service.UbsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/ubs")
public class UbsController {

    private final UbsService service;

    public UbsController(UbsService service) {
        this.service = service;
    }

    // POST /ubs é público – não há necessidade de token
    @PostMapping
    public ResponseEntity<UbsResponse> create(@Valid @RequestBody UbsRequest body) {
        return ResponseEntity.ok(service.create(body));
    }

    @GetMapping
    public List<UbsResponse> list() {
        return service.listAll();
    }

    @GetMapping("/{cnes}")
    public UbsResponse get(@PathVariable String cnes) {
        return service.getByCnes(cnes);
    }

    @PutMapping("/{cnes}")
    public UbsResponse update(@PathVariable String cnes,
                              @Valid @RequestBody UbsUpdateRequest body) {
        return service.update(cnes, body);
    }

    /* Integração com Medicamento‑service */
    @GetMapping("/{cnes}/medicamentos")
    public List<MedicamentoDTO> listMeds(@PathVariable String cnes) {
        return service.listarMedicamentosDaUbs(cnes);
    }
}
