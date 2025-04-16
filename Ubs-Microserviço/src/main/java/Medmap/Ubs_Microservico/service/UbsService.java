package Medmap.Ubs_Microservico.service;

import Medmap.Ubs_Microservico.client.AuthClient;
import Medmap.Ubs_Microservico.client.MedicamentoClient;
import Medmap.Ubs_Microservico.dto.*;
import Medmap.Ubs_Microservico.exception.ResourceAlreadyExistsException;
import Medmap.Ubs_Microservico.exception.ResourceNotFoundException;
import Medmap.Ubs_Microservico.model.Ubs;
import Medmap.Ubs_Microservico.repository.UbsRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    /* ---------- CREATE ---------- */
    @Transactional
    public UbsResponse create(UbsRequest req) {

        if (repo.existsByCnes(req.cnes()))
            throw new ResourceAlreadyExistsException("CNES já cadastrado");

        var body = new HashMap<String, Object>();
        body.put("nomeUbs", req.nome());
        body.put("cnes",    req.cnes());
        body.put("password",req.senha());

        try {
            authClient.registerUbs(body);          // **uma** chamada só
        } catch (FeignException e) {
            throw new ResponseStatusException(
                    HttpStatus.valueOf(e.status()),
                    e.contentUTF8().isBlank() ? "Auth‑service error" : e.contentUTF8(),
                    e);
        }

        /* salva metadados locais (sem senha) */
        Ubs ubs = new Ubs();
        ubs.setNome(req.nome());
        ubs.setCnes(req.cnes());
        ubs.setEndereco(req.endereco());
        repo.save(ubs);

        return map(ubs);
    }


    /* ---------- READ ---------- */
    public List<UbsResponse> listAll() {
        return repo.findAll().stream().map(this::map).toList();
    }

    public UbsResponse getByCnes(String cnes) {
        Ubs ubs = repo.findByCnes(cnes)
                .orElseThrow(() -> new ResourceNotFoundException("UBS não encontrada"));
        return map(ubs);
    }

    /* ---------- UPDATE ---------- */
    @Transactional
    public UbsResponse update(String cnes, UbsUpdateRequest req) {

        /* 1) garante que o token pertence à mesma UBS */
        String cnesDoToken = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        if (!cnes.equals(cnesDoToken)) {
            throw new AccessDeniedException("Não é permitido alterar outra UBS");
        }

        /* 2) atualiza registro */
        Ubs ubs = repo.findByCnes(cnes)
                .orElseThrow(() -> new ResourceNotFoundException("UBS não encontrada"));

        ubs.setNome(req.nome());
        ubs.setEndereco(req.endereco());
        return map(ubs);
    }

    /* ---------- Integração Medicamentos ---------- */
    public List<MedicamentoDTO> listarMedicamentosDaUbs(String cnes) {
        return medicamentoClient.listByUbs(cnes);
    }

    /* ---------- util ---------- */
    private UbsResponse map(Ubs u) {
        return new UbsResponse(u.getId(), u.getNome(), u.getCnes(), u.getEndereco());
    }
}
