package com.medicamento.medicamento_microservico.Model;

import com.medicamento.medicamento_microservico.Repository.MedicamentoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class MedicamentoTest {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Test
    void testMedicamentoPersistence() {
        Medicamento medicamento = new Medicamento();
        medicamento.setNome("Paracetamol");
        medicamento.setDescricao("Analgésico");
        medicamento.setEndereco("Rua A, 123");

        Medicamento savedMedicamento = medicamentoRepository.save(medicamento);

        assertNotNull(savedMedicamento.getId(), "O ID do medicamento não deveria ser nulo após salvar.");
        assertEquals("Paracetamol", savedMedicamento.getNome(), "O nome salvo não corresponde ao esperado.");
        assertEquals("Analgésico", savedMedicamento.getDescricao(), "A descrição salva não corresponde ao esperado.");
        assertEquals("Rua A, 123", savedMedicamento.getEndereco(), "O endereço salvo não corresponde ao esperado.");
    }

    @Test
    void testMedicamentoEquality() {
        Medicamento medicamento1 = new Medicamento();
        medicamento1.setNome("Ibuprofeno");
        medicamento1.setDescricao("Anti-inflamatório");
        medicamento1.setEndereco("Rua B, 456");

        Medicamento medicamento2 = new Medicamento();
        medicamento2.setNome("Ibuprofeno");
        medicamento2.setDescricao("Anti-inflamatório");
        medicamento2.setEndereco("Rua B, 456");

        // Comparar atributos individualmente
        assertEquals(medicamento1.getNome(), medicamento2.getNome(), "Os nomes devem ser iguais.");
        assertEquals(medicamento1.getDescricao(), medicamento2.getDescricao(), "As descrições devem ser iguais.");
        assertEquals(medicamento1.getEndereco(), medicamento2.getEndereco(), "Os endereços devem ser iguais.");

        // Garantir que os objetos não sejam exatamente iguais, pois id é null
        assertNotSame(medicamento1, medicamento2, "Os objetos não devem ser o mesmo na memória.");
    }


    @Test
    void testMedicamentoToString() {
        Medicamento medicamento = new Medicamento();
        medicamento.setNome("Amoxicilina");
        medicamento.setDescricao("Antibiótico");
        medicamento.setEndereco("Rua C, 789");

        String toStringOutput = medicamento.toString();
        assertTrue(toStringOutput.contains("Amoxicilina"), "O método toString deveria incluir o nome do medicamento.");
        assertTrue(toStringOutput.contains("Antibiótico"), "O método toString deveria incluir a descrição do medicamento.");
        assertTrue(toStringOutput.contains("Rua C, 789"), "O método toString deveria incluir o endereço do medicamento.");
    }
}
