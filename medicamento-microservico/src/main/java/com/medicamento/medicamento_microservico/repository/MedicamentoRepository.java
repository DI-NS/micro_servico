package com.medicamento.medicamento_microservico.repository;

import com.medicamento.medicamento_microservico.model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {

    // Agora inclui ubsId para escopo por UBS
    Optional<Medicamento> findByNomeAndInformacoesAndUbsId(
            String nome,
            String informacoes,
            Long ubsId
    );
}
