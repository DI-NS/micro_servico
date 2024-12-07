package com.medicamento.medicamento_microservico.Repository;

import com.medicamento.medicamento_microservico.Model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {
    // Método para buscar um medicamento pelo nome e descrição
    Optional<Medicamento> findByNomeAndDescricao(String nome, String descricao);
}
