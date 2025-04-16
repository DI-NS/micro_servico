package com.medicamento.medicamento_microservico.Service;

import com.medicamento.medicamento_microservico.Model.Medicamento;
import com.medicamento.medicamento_microservico.Repository.MedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class MedicamentoService {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Transactional
    public Medicamento create(Medicamento medicamento) {
        if (medicamento.getNome() == null || medicamento.getNome().isBlank()) {
            throw new RuntimeException("Erro: O nome do medicamento é obrigatório.");
        }
        if (medicamento.getInformacoes() == null || medicamento.getInformacoes().isBlank()) {
            throw new RuntimeException("Erro: As informações do medicamento são obrigatórias.");
        }
        if (medicamento.getImagemUrl() == null || medicamento.getImagemUrl().isBlank()) {
            throw new RuntimeException("Erro: A URL da imagem do medicamento é obrigatória.");
        }
        if (medicamento.getUbsId() == null) {
            throw new RuntimeException("Erro: O ID da UBS é obrigatório.");
        }
        Optional<Medicamento> medicamentoExistente = medicamentoRepository.findByNomeAndInformacoes(
                medicamento.getNome(),
                medicamento.getInformacoes()
        );
        if (medicamentoExistente.isPresent()) {
            throw new RuntimeException("Erro: Já existe um medicamento com o mesmo nome e informações.");
        }
        return medicamentoRepository.save(medicamento);
    }

    public List<Medicamento> findAll() {
        List<Medicamento> medicamentos = medicamentoRepository.findAll();
        if (medicamentos.isEmpty()) {
            throw new RuntimeException("Erro: Nenhum medicamento encontrado.");
        }
        return medicamentos;
    }

    public Medicamento findById(Long id) {
        if (id == null) {
            throw new RuntimeException("Erro: O ID fornecido é nulo.");
        }
        return medicamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro: Medicamento com ID " + id + " não encontrado."));
    }

    public Medicamento update(Long id, Medicamento medicamentoDetails) {
        if (id == null || medicamentoDetails == null) {
            throw new RuntimeException("Erro: O ID ou os detalhes do medicamento não podem ser nulos.");
        }
        Medicamento medicamento = findById(id);
        medicamento.setNome(medicamentoDetails.getNome());
        medicamento.setInformacoes(medicamentoDetails.getInformacoes());
        medicamento.setImagemUrl(medicamentoDetails.getImagemUrl());
        medicamento.setAtivo(medicamentoDetails.isAtivo());
        medicamento.setUbsId(medicamentoDetails.getUbsId());
        return medicamentoRepository.save(medicamento);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new RuntimeException("Erro: O ID fornecido é nulo.");
        }
        if (!medicamentoRepository.existsById(id)) {
            throw new RuntimeException("Erro: Medicamento com ID " + id + " não encontrado.");
        }
        medicamentoRepository.deleteById(id);
    }
}
