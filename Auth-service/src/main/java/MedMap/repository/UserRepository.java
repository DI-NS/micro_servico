package MedMap.repository;

import MedMap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositório para operações com a entidade User.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca uma UBS pelo nome e CNES.
     *
     * @param nomeUbs Nome da UBS.
     * @param cnes Código CNES da UBS.
     * @return Optional contendo a UBS, se encontrada.
     */
    Optional<User> findByNomeUbsAndCnes(String nomeUbs, String cnes);

    /**
     * Busca uma UBS pelo CNES.
     *
     * @param cnes Código CNES da UBS.
     * @return Optional contendo a UBS, se encontrada.
     */
    Optional<User> findByCnes(String cnes);
}
