package com.usuario.usuario_microservico.Model;

import lombok.Data;

@Data
public class Medicamento {
    private Long id;
    private String nome;
    private String informacoes;
    private String imagemUrl;
    private boolean ativo;
    private Long ubsId;
}
