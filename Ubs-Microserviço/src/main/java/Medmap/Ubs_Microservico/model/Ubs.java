package Medmap.Ubs_Microservico.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade UBS – não armazena senha!
 * Tabela: ubs (id, nome, cnes, endereco)
 */
@Getter @Setter @NoArgsConstructor
@Entity @Table(name = "ubs")
public class Ubs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank private String nome;

    @Column(unique = true, nullable = false)
    @NotBlank private String cnes;

    @NotBlank private String endereco;
}
