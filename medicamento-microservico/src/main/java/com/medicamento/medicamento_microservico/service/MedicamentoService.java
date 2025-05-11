package com.medicamento.medicamento_microservico.service;

import com.medicamento.medicamento_microservico.model.Medicamento;
import com.medicamento.medicamento_microservico.repository.MedicamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MedicamentoService {

    private final MedicamentoRepository repo;

    public MedicamentoService(MedicamentoRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Medicamento create(Medicamento m) {
        // mantém validação de duplicado dentro da mesma UBS
        repo.findByNomeAndInformacoesAndUbsId(
                m.getNome(),
                m.getInformacoes(),
                m.getUbsId()
        ).ifPresent(x -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Já existe um medicamento com este nome e informações nesta UBS");
        });
        return repo.save(m);
    }

    public List<Medicamento> findAll() {
        var l = repo.findAll();
        if (l.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum medicamento encontrado");
        }
        return l;
    }

    /**
     * NOVO: busca só os remédios cadastrados pela UBS passada
     */
    public List<Medicamento> findByUbsId(Long ubsId) {
        var l = repo.findAllByUbsId(ubsId);
        if (l.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Nenhum medicamento encontrado para essa UBS"
            );
        }
        return l;
    }

    public Medicamento findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Medicamento com ID " + id + " não encontrado"
                ));
    }

    @Transactional
    public Medicamento update(Long id, Medicamento m) {
        Medicamento existing = findById(id);

        // barrar conflito se trocar nome/informações dentro da mesma UBS
        if (!existing.getNome().equals(m.getNome())
                || !existing.getInformacoes().equals(m.getInformacoes())) {

            repo.findByNomeAndInformacoesAndUbsId(
                    m.getNome(),
                    m.getInformacoes(),
                    existing.getUbsId()
            ).ifPresent(x -> {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Já existe outro medicamento com este nome e informações nesta UBS");
            });
        }

        existing.setNome(m.getNome());
        existing.setInformacoes(m.getInformacoes());
        existing.setImagemUrl(m.getImagemUrl());
        existing.setAtivo(m.isAtivo());
        // ubsId não é alterado aqui
        return repo.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Medicamento com ID " + id + " não encontrado");
        }
        repo.deleteById(id);
    }
}
