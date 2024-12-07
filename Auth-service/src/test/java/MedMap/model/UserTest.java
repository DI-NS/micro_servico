package MedMap.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa getters e setters da entidade User.
 */
class UserTest {

    @Test
    void testUserGettersSetters() {
        User user = new User();
        user.setNomeUbs("UBS Test");
        user.setCnes("123456");
        user.setPassword("myPass");
        user.setHashedPassword("hashedPass");
        user.setAddress("Address");

        assertEquals("UBS Test", user.getNomeUbs());
        assertEquals("123456", user.getCnes());
        assertEquals("myPass", user.getPassword());
        assertEquals("hashedPass", user.getHashedPassword());
        assertEquals("Address", user.getAddress());
        // Não testamos ID pois não foi persistido.
    }
}
