package Medmap.Ubs_Microservico.dto;

public record UbsResponse(
        Long id,
        String nome,
        String cnes,
        String endereco
) {}
