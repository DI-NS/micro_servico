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

        // Verificar se os campos obrigatórios estão preenchidos
        if (medicamento.getNome() == null || medicamento.getNome().isBlank()) {
            throw new RuntimeException("Erro: O nome do medicamento é obrigatório.");
        }
        if (medicamento.getDescricao() == null || medicamento.getDescricao().isBlank()) {
            throw new RuntimeException("Erro: A descrição do medicamento é obrigatória.");
        }
        if (medicamento.getEndereco() == null || medicamento.getEndereco().isBlank()) {
            throw new RuntimeException("Erro: O endereço do medicamento é obrigatório.");
        }

        // Verificar duplicação de nome e descrição
        Optional<Medicamento> medicamentoExistente = medicamentoRepository.findByNomeAndDescricao(
                medicamento.getNome(),
                medicamento.getDescricao()
        );
        if (medicamentoExistente.isPresent()) {
            throw new RuntimeException("Erro: Já existe um medicamento com o mesmo nome e descrição.");
        }


        // Criar o registro
        return medicamentoRepository.save(medicamento);
    }

    public List<Medicamento> findAll() {

        //Listar todos os medicamentos
        List<Medicamento> medicamentos = medicamentoRepository.findAll();
        if (medicamentos.isEmpty()) {
            throw new RuntimeException("Erro: Nenhum medicamento encontrado.");
        }
        return medicamentos;
    }

    public Medicamento findById(Long id) {

        //Exibir um remédio específico pelo ID
        if (id == null) {
            throw new RuntimeException("Erro: O ID fornecido é nulo.");
        }
        return medicamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro: Medicamento com ID " + id + " não encontrado."));
    }

    public Medicamento update(Long id, Medicamento medicamentoDetails) {

        //Atualizar informações do remédio
        if (id == null || medicamentoDetails == null) {
            throw new RuntimeException("Erro: O ID ou os detalhes do medicamento não podem ser nulos.");
        }
        Medicamento medicamento = findById(id);
        medicamento.setNome(medicamentoDetails.getNome());
        medicamento.setDescricao(medicamentoDetails.getDescricao());
        medicamento.setEndereco(medicamentoDetails.getEndereco());
        return medicamentoRepository.save(medicamento);
    }

    public void delete(Long id) {

        //Deletar um medicamento
        if (id == null) {
            throw new RuntimeException("Erro: O ID fornecido é nulo.");
        }
        if (!medicamentoRepository.existsById(id)) {
            throw new RuntimeException("Erro: Medicamento com ID " + id + " não encontrado.");
        }
        medicamentoRepository.deleteById(id);
    }

}
