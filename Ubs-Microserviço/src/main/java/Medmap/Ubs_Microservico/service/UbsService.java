package Medmap.Ubs_Microservico.service;

import Medmap.Ubs_Microservico.client.AuthClient;
import Medmap.Ubs_Microservico.client.MedicamentoClient;
import Medmap.Ubs_Microservico.dto.*;
import Medmap.Ubs_Microservico.exception.ResourceAlreadyExistsException;
import Medmap.Ubs_Microservico.exception.ResourceNotFoundException;
import Medmap.Ubs_Microservico.model.Ubs;
import Medmap.Ubs_Microservico.repository.UbsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;

@Service
public class UbsService {

    private final UbsRepository repo;
    private final AuthClient authClient;
    private final MedicamentoClient medicamentoClient;

    public UbsService(UbsRepository repo,
                      AuthClient authClient,
                      MedicamentoClient medicamentoClient) {
        this.repo = repo;
        this.authClient = authClient;
        this.medicamentoClient = medicamentoClient;
    }

    @Transactional
    public UbsResponse create(UbsRequest req) {
        if (repo.existsByCnes(req.cnes()))
            throw new ResourceAlreadyExistsException("CNES já cadastrado");

        // Registra credenciais no Auth‑service (envia nomeUbs, cnes e senha)
        var body = new HashMap<String, Object>();
        body.put("nomeUbs", req.nome());
        body.put("cnes", req.cnes());
        body.put("password", req.senha());
        authClient.registerUbs(body);

        // Persiste metadados locais (incluindo senha)
        Ubs ubs = new Ubs();
        ubs.setNome(req.nome());
        ubs.setCnes(req.cnes());
        ubs.setEndereco(req.endereco());
        ubs.setSenha(req.senha());
        repo.save(ubs);

        return map(ubs);
    }

    public List<UbsResponse> listAll() {
        return repo.findAll().stream().map(this::map).toList();
    }

    public UbsResponse getByCnes(String cnes) {
        Ubs ubs = repo.findByCnes(cnes)
                .orElseThrow(() -> new ResourceNotFoundException("UBS não encontrada"));
        return map(ubs);
    }

    @Transactional
    public UbsResponse update(String cnes, UbsUpdateRequest req) {
        Ubs ubs = repo.findByCnes(cnes)
                .orElseThrow(() -> new ResourceNotFoundException("UBS não encontrada"));
        ubs.setNome(req.nome());
        ubs.setEndereco(req.endereco());
        return map(ubs);
    }

    // Integração com o Medicamento‑service (mantém o mesmo)
    public List<MedicamentoDTO> listarMedicamentosDaUbs(String cnes) {
        return medicamentoClient.listByUbs(cnes);
    }

    private UbsResponse map(Ubs u) {
        return new UbsResponse(u.getId(), u.getNome(), u.getCnes(), u.getEndereco());
    }
}
