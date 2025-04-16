package com.medicamento.medicamento_microservico.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "medicamentos")
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String nome;

    // Representa as informações detalhadas sobre o medicamento.
    @Column(name = "informacoes", nullable = false)
    private String informacoes;

    // URL da imagem do medicamento.
    @Column(name = "imagem_url")
    private String imagemUrl;

    // Flag que indica se o medicamento está ativo.
    private boolean ativo;

    // Identificador da UBS (foreign key) que cadastrou o medicamento.
    @Column(name = "ubs_id", nullable = false)
    private Long ubsId;
}
