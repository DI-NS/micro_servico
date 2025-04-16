package Medmap.Ubs_Microservico.dto;

import jakarta.validation.constraints.NotBlank;

public record UbsRequest(
        @NotBlank String nome,
        @NotBlank String cnes,
        @NotBlank String endereco,
        @NotBlank String senha      // só na criação
) {}

