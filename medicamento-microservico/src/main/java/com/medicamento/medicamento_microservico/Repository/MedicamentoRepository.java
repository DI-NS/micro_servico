package com.medicamento.medicamento_microservico.Repository;

import com.medicamento.medicamento_microservico.Model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {

    // Busca um medicamento pelo nome e pelas informações.
    Optional<Medicamento> findByNomeAndInformacoes(String nome, String informacoes);
}
