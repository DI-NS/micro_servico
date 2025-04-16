package Medmap.Ubs_Microservico.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade UBS armazenada no banco local.
 * A senha **não** é guardada aqui – fica no Auth‑service.
 */
@Getter @Setter @NoArgsConstructor
@Entity @Table(name = "ubs")
public class Ubs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome da UBS é obrigatório")
    private String nome;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "CNES é obrigatório")
    private String cnes;

    @NotBlank(message = "Endereço é obrigatório")
    private String endereco;

    /** flag para desativar UBS sem apagar do banco */
    private boolean ativa = true;
}
