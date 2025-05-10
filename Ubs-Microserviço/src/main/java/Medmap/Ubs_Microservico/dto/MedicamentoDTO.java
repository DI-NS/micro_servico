package Medmap.Ubs_Microservico.dto;

/**
 * Projeção recebida do microserviço de Medicamentos.
 */
public record MedicamentoDTO(
        Long id,
        String nome,
        String descricao,
        boolean ativo
) {}
