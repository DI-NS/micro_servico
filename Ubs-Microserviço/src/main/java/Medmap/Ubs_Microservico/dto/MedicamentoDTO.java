package Medmap.Ubs_Microservico.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Projeção recebida do microserviço de Medicamentos.
 */
public record MedicamentoDTO(
        Long id,
        String nome,

        @JsonProperty("informacoes")
        String descricao,

        boolean ativo
) {}
