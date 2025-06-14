package Medmap.Ubs_Microservico.client;

/** Projeção vinda do Medicamento‑service */
public record MedicamentoDTO(
        Long id,
        String nome,
        String descricao,
        boolean ativo
) {}