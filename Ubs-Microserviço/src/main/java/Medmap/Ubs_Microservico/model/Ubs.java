package Medmap.Ubs_Microservico.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade UBS armazenada no banco local.
 * Agora, a tabela "ubs" conterá: ID, nome, CNES, endereço e senha.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ubs")
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

    @NotBlank(message = "Senha é obrigatória")
    private String senha;
}
