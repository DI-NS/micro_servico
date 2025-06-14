package Medmap.Ubs_Microservico.dto;

import jakarta.validation.constraints.NotBlank;

public record UbsUpdateRequest(
        @NotBlank String nome,
        @NotBlank String endereco
) {}
