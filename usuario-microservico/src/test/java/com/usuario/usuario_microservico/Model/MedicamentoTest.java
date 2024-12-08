package com.usuario.usuario_microservico.Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MedicamentoTest {

    @Test
    void testMedicamentoAttributes() {
        // Criando um objeto Medicamento
        Medicamento medicamento = new Medicamento();

        // Definindo valores
        medicamento.setNome("Paracetamol");
        medicamento.setDescricao("Analgésico");
        medicamento.setEndereco("Rua A, 123");

        // Verificando os valores definidos
        assertEquals("Paracetamol", medicamento.getNome());
        assertEquals("Analgésico", medicamento.getDescricao());
        assertEquals("Rua A, 123", medicamento.getEndereco());
    }

    @Test
    void testMedicamentoEquality() {
        // Criando dois objetos Medicamento com os mesmos atributos
        Medicamento medicamento1 = new Medicamento();
        medicamento1.setNome("Ibuprofeno");
        medicamento1.setDescricao("Anti-inflamatório");
        medicamento1.setEndereco("Rua B, 456");

        Medicamento medicamento2 = new Medicamento();
        medicamento2.setNome("Ibuprofeno");
        medicamento2.setDescricao("Anti-inflamatório");
        medicamento2.setEndereco("Rua B, 456");

        // Verificando igualdade com base nos atributos, não na instância
        assertEquals(medicamento1.getNome(), medicamento2.getNome());
        assertEquals(medicamento1.getDescricao(), medicamento2.getDescricao());
        assertEquals(medicamento1.getEndereco(), medicamento2.getEndereco());
    }


    @Test
    void testToString() {
        // Criando um objeto Medicamento
        Medicamento medicamento = new Medicamento();
        medicamento.setNome("Aspirina");
        medicamento.setDescricao("Anti-inflamatório");
        medicamento.setEndereco("Rua C, 789");

        // Verificando o método toString
        String expectedString = "Medicamento(id=null, nome=Aspirina, descricao=Anti-inflamatório, endereco=Rua C, 789)";
        assertEquals(expectedString, medicamento.toString());
    }
}
