package MedMap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Representa uma UBS (Unidade Básica de Saúde) no sistema MedMap.
 * CNES funciona como um identificador público (username).
 * A senha será armazenada de forma hasheada em hashedPassword.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @JsonIgnore // O ID não é relevante para o usuário
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
     * Senha em texto puro não será armazenada no banco.
     * Será utilizada apenas para receber o valor na requisição.
     */
    @Transient
    @NotBlank(message = "A senha é obrigatória")
    private String password;

    /**
     * Senha hasheada, armazenada de forma segura no banco.
     */
    @JsonIgnore // Não expor o hash da senha.
    @Column(name = "hashed_password", nullable = false)
    private String hashedPassword;

    /**
     * Endereço da UBS.
     */
    @Column(nullable = false)
    @NotBlank(message = "O endereço é obrigatório")
    private String address;

    // Construtores
    public User() {
    }

    public User(String nomeUbs, String cnes, String address, String password) {
        this.nomeUbs = nomeUbs;
        this.cnes = cnes;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
