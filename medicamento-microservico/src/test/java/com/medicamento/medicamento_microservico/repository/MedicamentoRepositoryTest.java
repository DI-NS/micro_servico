package com.medicamento.medicamento_microservico.repository;

import com.medicamento.medicamento_microservico.model.Medicamento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class MedicamentoRepositoryTest {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "jdbc:h2:mem:testdb");
        registry.add("spring.datasource.driver-class-name", () -> "org.h2.Driver");
        registry.add("spring.datasource.username", () -> "sa");
        registry.add("spring.datasource.password", () -> "");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.H2Dialect");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Test
    void testSaveAndRetrieveMedicamento() {
        Medicamento medicamento = new Medicamento();
        medicamento.setNome("Dipirona");
        medicamento.setDescricao("Analgésico");
        medicamento.setEndereco("Rua B, 456");

        Medicamento saved = medicamentoRepository.save(medicamento);

        Optional<Medicamento> found = medicamentoRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Dipirona", found.get().getNome());
        assertEquals("Analgésico", found.get().getDescricao());
    }

    @Test
    void testFindByNomeAndDescricao_WhenExists() {
        Medicamento medicamento = new Medicamento();
        medicamento.setNome("Paracetamol");
        medicamento.setDescricao("Antitérmico");
        medicamento.setEndereco("Rua C, 789");

        medicamentoRepository.save(medicamento);

        Optional<Medicamento> found = medicamentoRepository.findByNomeAndDescricao("Paracetamol", "Antitérmico");
        assertTrue(found.isPresent());
        assertEquals("Paracetamol", found.get().getNome());
        assertEquals("Antitérmico", found.get().getDescricao());
    }

    @Test
    void testFindByNomeAndDescricao_WhenNotExists() {
        Optional<Medicamento> found = medicamentoRepository.findByNomeAndDescricao("Ibuprofeno", "Antiinflamatório");
        assertFalse(found.isPresent());
    }

    @Test
    void testDeleteMedicamento() {
        Medicamento medicamento = new Medicamento();
        medicamento.setNome("Amoxicilina");
        medicamento.setDescricao("Antibiótico");
        medicamento.setEndereco("Rua D, 123");

        Medicamento saved = medicamentoRepository.save(medicamento);

        medicamentoRepository.deleteById(saved.getId());

        assertFalse(medicamentoRepository.existsById(saved.getId()));
    }
}
