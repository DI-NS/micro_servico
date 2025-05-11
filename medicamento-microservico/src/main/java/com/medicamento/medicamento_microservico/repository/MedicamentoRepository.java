package com.medicamento.medicamento_microservico.repository;

import com.medicamento.medicamento_microservico.model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {

    // já existia, para evitar duplicação ao criar/atualizar
    Optional<Medicamento> findByNomeAndInformacoesAndUbsId(
            String nome,
            String informacoes,
            Long ubsId
    );

    // NOVO: busca todos os remédios de uma UBS
    List<Medicamento> findAllByUbsId(Long ubsId);
}
