package com.medicamento.medicamento_microservico.Service;

import com.medicamento.medicamento_microservico.Model.Medicamento;
import com.medicamento.medicamento_microservico.Repository.MedicamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicamentoServiceTest {

    @InjectMocks
    private MedicamentoService medicamentoService;

    @Mock
    private MedicamentoRepository medicamentoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_ShouldCreateMedicamento_WhenValidDataProvided() {
        Medicamento medicamento = new Medicamento();
        medicamento.setNome("Paracetamol");
        medicamento.setDescricao("Analgésico");
        medicamento.setEndereco("Rua 123");

        when(medicamentoRepository.findByNomeAndDescricao(medicamento.getNome(), medicamento.getDescricao()))
                .thenReturn(Optional.empty());
        when(medicamentoRepository.save(medicamento)).thenReturn(medicamento);

        Medicamento createdMedicamento = medicamentoService.create(medicamento);

        assertNotNull(createdMedicamento);
        assertEquals("Paracetamol", createdMedicamento.getNome());
        verify(medicamentoRepository, times(1)).save(medicamento);
    }

    @Test
    void create_ShouldThrowException_WhenNomeIsNull() {
        Medicamento medicamento = new Medicamento();
        medicamento.setDescricao("Analgésico");
        medicamento.setEndereco("Rua 123");

        Exception exception = assertThrows(RuntimeException.class, () -> medicamentoService.create(medicamento));

        assertEquals("Erro: O nome do medicamento é obrigatório.", exception.getMessage());
    }

    @Test
    void create_ShouldThrowException_WhenDuplicateMedicamentoExists() {
        Medicamento medicamento = new Medicamento();
        medicamento.setNome("Paracetamol");
        medicamento.setDescricao("Analgésico");
        medicamento.setEndereco("Rua 123");

        when(medicamentoRepository.findByNomeAndDescricao(medicamento.getNome(), medicamento.getDescricao()))
                .thenReturn(Optional.of(medicamento));

        Exception exception = assertThrows(RuntimeException.class, () -> medicamentoService.create(medicamento));

        assertEquals("Erro: Já existe um medicamento com o mesmo nome e descrição.", exception.getMessage());
        verify(medicamentoRepository, never()).save(medicamento);
    }

    @Test
    void findAll_ShouldReturnListOfMedicamentos_WhenMedicamentosExist() {
        Medicamento medicamento1 = new Medicamento();
        medicamento1.setNome("Paracetamol");
        medicamento1.setDescricao("Analgésico");
        medicamento1.setEndereco("Rua 123");

        Medicamento medicamento2 = new Medicamento();
        medicamento2.setNome("Ibuprofeno");
        medicamento2.setDescricao("Antiinflamatório");
        medicamento2.setEndereco("Rua 456");

        when(medicamentoRepository.findAll()).thenReturn(Arrays.asList(medicamento1, medicamento2));

        List<Medicamento> medicamentos = medicamentoService.findAll();

        assertEquals(2, medicamentos.size());
        verify(medicamentoRepository, times(1)).findAll();
    }

    @Test
    void findAll_ShouldThrowException_WhenNoMedicamentosFound() {
        when(medicamentoRepository.findAll()).thenReturn(List.of());

        Exception exception = assertThrows(RuntimeException.class, () -> medicamentoService.findAll());

        assertEquals("Erro: Nenhum medicamento encontrado.", exception.getMessage());
    }

    @Test
    void findById_ShouldReturnMedicamento_WhenValidIdProvided() {
        Medicamento medicamento = new Medicamento();
        medicamento.setId(1L);
        medicamento.setNome("Paracetamol");

        when(medicamentoRepository.findById(1L)).thenReturn(Optional.of(medicamento));

        Medicamento foundMedicamento = medicamentoService.findById(1L);

        assertNotNull(foundMedicamento);
        assertEquals(1L, foundMedicamento.getId());
        verify(medicamentoRepository, times(1)).findById(1L);
    }

    @Test
    void findById_ShouldThrowException_WhenInvalidIdProvided() {
        when(medicamentoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> medicamentoService.findById(1L));

        assertEquals("Erro: Medicamento com ID 1 não encontrado.", exception.getMessage());
    }
    @Test
    void create_ShouldThrowException_WhenDescricaoIsNull() {
        Medicamento medicamento = new Medicamento();
        medicamento.setNome("Paracetamol");
        medicamento.setEndereco("Rua 123");

        Exception exception = assertThrows(RuntimeException.class, () -> medicamentoService.create(medicamento));

        assertEquals("Erro: A descrição do medicamento é obrigatória.", exception.getMessage());
    }

    @Test
    void create_ShouldThrowException_WhenEnderecoIsBlank() {
        Medicamento medicamento = new Medicamento();
        medicamento.setNome("Paracetamol");
        medicamento.setDescricao("Analgésico");
        medicamento.setEndereco("");

        Exception exception = assertThrows(RuntimeException.class, () -> medicamentoService.create(medicamento));

        assertEquals("Erro: O endereço do medicamento é obrigatório.", exception.getMessage());
    }

    @Test
    void findById_ShouldThrowException_WhenIdIsNull() {
        Exception exception = assertThrows(RuntimeException.class, () -> medicamentoService.findById(null));

        assertEquals("Erro: O ID fornecido é nulo.", exception.getMessage());
    }

    @Test
    void update_ShouldThrowException_WhenIdIsNull() {
        Medicamento medicamentoDetails = new Medicamento();
        medicamentoDetails.setNome("Ibuprofeno");

        Exception exception = assertThrows(RuntimeException.class, () -> medicamentoService.update(null, medicamentoDetails));

        assertEquals("Erro: O ID ou os detalhes do medicamento não podem ser nulos.", exception.getMessage());
    }

    @Test
    void update_ShouldThrowException_WhenMedicamentoDetailsIsNull() {
        Exception exception = assertThrows(RuntimeException.class, () -> medicamentoService.update(1L, null));

        assertEquals("Erro: O ID ou os detalhes do medicamento não podem ser nulos.", exception.getMessage());
    }

    @Test
    void delete_ShouldThrowException_WhenIdIsNull() {
        Exception exception = assertThrows(RuntimeException.class, () -> medicamentoService.delete(null));

        assertEquals("Erro: O ID fornecido é nulo.", exception.getMessage());
    }

    @Test
    void update_ShouldUpdateMedicamento_WhenValidIdAndDetailsProvided() {
        Medicamento existingMedicamento = new Medicamento();
        existingMedicamento.setId(1L);
        existingMedicamento.setNome("Paracetamol");

        Medicamento updatedDetails = new Medicamento();
        updatedDetails.setNome("Ibuprofeno");
        updatedDetails.setDescricao("Antiinflamatório");
        updatedDetails.setEndereco("Rua 456");

        when(medicamentoRepository.findById(1L)).thenReturn(Optional.of(existingMedicamento));
        when(medicamentoRepository.save(existingMedicamento)).thenReturn(existingMedicamento);

        Medicamento updatedMedicamento = medicamentoService.update(1L, updatedDetails);

        assertNotNull(updatedMedicamento);
        assertEquals("Ibuprofeno", updatedMedicamento.getNome());
        verify(medicamentoRepository, times(1)).save(existingMedicamento);
    }

    @Test
    void delete_ShouldDeleteMedicamento_WhenValidIdProvided() {
        when(medicamentoRepository.existsById(1L)).thenReturn(true);

        medicamentoService.delete(1L);

        verify(medicamentoRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowException_WhenInvalidIdProvided() {
        when(medicamentoRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> medicamentoService.delete(1L));

        assertEquals("Erro: Medicamento com ID 1 não encontrado.", exception.getMessage());
        verify(medicamentoRepository, never()).deleteById(anyLong());
    }
}
