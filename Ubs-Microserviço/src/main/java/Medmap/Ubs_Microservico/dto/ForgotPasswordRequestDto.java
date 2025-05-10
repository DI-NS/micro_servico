package Medmap.Ubs_Microservico.dto;

public record ForgotPasswordRequestDto(
        String nomeUbs,
        String cnes,
        String newPassword
) {}
