package com.usuario.usuario_microservico.dto;

public class UbsInfoDTO {
    private Long id;
    private String cnes;
    private String nome;
    private String endereco;

    public UbsInfoDTO() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCnes() {
        return cnes;
    }
    public void setCnes(String cnes) {
        this.cnes = cnes;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
