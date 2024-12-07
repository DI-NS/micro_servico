package MedMap.repository;

import MedMap.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa o UserRepository, verificando busca por CNES.
 */
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve encontrar uma UBS pelo CNES")
    void shouldFindUserByCnes() {
        User user = new User("UBS Teste", "999999", "Rua Teste", "senha");
        user.setHashedPassword("hash");
        userRepository.save(user);

        Optional<User> found = userRepository.findByCnes("999999");
        assertTrue(found.isPresent());
        assertEquals("UBS Teste", found.get().getNomeUbs());
    }

    @Test
    @DisplayName("Deve retornar vazio se CNES não existe")
    void shouldReturnEmptyIfCnesNotFound() {
        Optional<User> found = userRepository.findByCnes("inexistente");
        assertFalse(found.isPresent());
    }
}
