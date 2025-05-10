package MedMap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Representa uma UBS (Unidade Básica de Saúde) no sistema MedMap.
 * O CNES funciona como identificador público (username).
 * A senha em texto não é armazenada no banco; somente o hash é persistido.
 *
 * Esquema da tabela "usuarios": ID, nome_ubs, cnes, senha_hash.
 */
@Entity
@Table(name = "usuarios")
public class User {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome da UBS.
     */
    @Column(name = "nome_ubs", nullable = false)
    @NotBlank(message = "O nome da UBS é obrigatório")
    private String nomeUbs;

    /**
     * Código CNES da UBS (identificador público).
     */
    @Column(name = "cnes", nullable = false, unique = true)
    @NotBlank(message = "O CNES é obrigatório")
    private String cnes;

    /**
     * Senha em texto puro, não será armazenada no banco.
     */
    @Transient
    @NotBlank(message = "A senha é obrigatória")
    private String password;

    /**
     * Senha hasheada, armazenada de forma segura no banco.
     */
    @JsonIgnore
    @Column(name = "senha_hash", nullable = false)
    private String hashedPassword;

    // Construtores
    public User() {
    }

    public User(String nomeUbs, String cnes, String password) {
        this.nomeUbs = nomeUbs;
        this.cnes = cnes;
        this.password = password;
    }

    // Getters e Setters
    public Long getId() {
        return id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
}
