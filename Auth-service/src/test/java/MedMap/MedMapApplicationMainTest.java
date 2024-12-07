package MedMap;

import org.junit.jupiter.api.Test;

/**
 * Testa se a aplicação inicializa sem erros.
 */
class MedMapApplicationMainTest {

    @Test
    void testMainMethod() {
        MedMapApplication.main(new String[]{});
        // Verifica se o contexto sobe sem exceções.
    }
}
