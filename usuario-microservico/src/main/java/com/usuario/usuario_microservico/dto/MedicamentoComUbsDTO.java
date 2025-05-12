package com.usuario.usuario_microservico.dto;

public class MedicamentoComUbsDTO {
    private Long id;
    private String nome;
    private String informacoes;
    private String imagemUrl;
    private boolean ativo;
    private Long ubsId;

    // campos novos
    private String ubsCnes;
    private String ubsNome;
    private String ubsEndereco;

    // Construtor vazio
    public MedicamentoComUbsDTO() {}

    // Getters e Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getInformacoes() {
        return informacoes;
    }
    public void setInformacoes(String informacoes) {
        this.informacoes = informacoes;
    }
    public String getImagemUrl() {
        return imagemUrl;
    }
    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }
    public boolean isAtivo() {
        return ativo;
    }
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    public Long getUbsId() {
        return ubsId;
    }
    public void setUbsId(Long ubsId) {
        this.ubsId = ubsId;
    }
    public String getUbsCnes() {
        return ubsCnes;
    }
    public void setUbsCnes(String ubsCnes) {
        this.ubsCnes = ubsCnes;
    }
    public String getUbsNome() {
        return ubsNome;
    }
    public void setUbsNome(String ubsNome) {
        this.ubsNome = ubsNome;
    }
    public String getUbsEndereco() {
        return ubsEndereco;
    }
    public void setUbsEndereco(String ubsEndereco) {
        this.ubsEndereco = ubsEndereco;
    }
}
