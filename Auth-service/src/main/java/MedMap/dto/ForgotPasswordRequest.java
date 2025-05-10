package MedMap.dto;

import jakarta.validation.constraints.NotBlank;

public class ForgotPasswordRequest {

    @NotBlank
    private String nomeUbs;

    @NotBlank
    private String cnes;

    @NotBlank
    private String newPassword;

    public ForgotPasswordRequest() {}

    public ForgotPasswordRequest(String nomeUbs, String cnes, String newPassword) {
        this.nomeUbs = nomeUbs;
        this.cnes = cnes;
        this.newPassword = newPassword;
    }

    public String getNomeUbs() {
        return nomeUbs;
    }

    public void setNomeUbs(String nomeUbs) {
        this.nomeUbs = nomeUbs;
    }

    public String getCnes() {
        return cnes;
    }

    public void setCnes(String cnes) {
        this.cnes = cnes;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
