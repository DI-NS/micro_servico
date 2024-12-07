package com.medicamento.medicamento_microservico.Service;

import com.medicamento.medicamento_microservico.Model.Medicamento;
import com.medicamento.medicamento_microservico.Repository.MedicamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
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
    void testCreateMedicamento_Success() {
        Medicamento medicamento = new Medicamento();
        medicamento.setNome("Paracetamol");
        medicamento.setDescricao("Analgésico");
        medicamento.setEndereco("Rua A");

        when(medicamentoRepository.findByNomeAndDescricao("Paracetamol", "Analgésico"))
                .thenReturn(Optional.empty());
        when(medicamentoRepository.save(medicamento)).thenReturn(medicamento);

        Medicamento result = medicamentoService.create(medicamento);

        assertNotNull(result);
        assertEquals("Paracetamol", result.getNome());
        verify(medicamentoRepository, times(1)).save(medicamento);
    }

    @Test
    void testCreateMedicamento_MissingFields() {
        Medicamento medicamento = new Medicamento();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> medicamentoService.create(medicamento));

        assertTrue(exception.getMessage().contains("O nome do medicamento é obrigatório."));
        verify(medicamentoRepository, never()).save(medicamento);
    }

    @Test
    void testCreateMedicamento_Duplicate() {

        Medicamento medicamento = new Medicamento();
        medicamento.setNome("Paracetamol");
        medicamento.setDescricao("Analgésico");
        medicamento.setEndereco("Rua A, 123");


        when(medicamentoRepository.findByNomeAndDescricao("Paracetamol", "Analgésico"))
                .thenReturn(Optional.of(new Medicamento()));


        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> medicamentoService.create(medicamento),
                "Erro esperado ao tentar cadastrar medicamento duplicado."
        );


        assertEquals(
                "Erro: Já existe um medicamento com o mesmo nome e descrição.",
                exception.getMessage()
        );


        verify(medicamentoRepository, never()).save(any());
    }



    @Test
    void testFindAll_Success() {
        Medicamento medicamento1 = new Medicamento();
        medicamento1.setNome("Paracetamol");

        Medicamento medicamento2 = new Medicamento();
        medicamento2.setNome("Ibuprofeno");

        when(medicamentoRepository.findAll()).thenReturn(Arrays.asList(medicamento1, medicamento2));

        var medicamentos = medicamentoService.findAll();

        assertEquals(2, medicamentos.size());
        verify(medicamentoRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_EmptyList() {
        when(medicamentoRepository.findAll()).thenReturn(Arrays.asList());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> medicamentoService.findAll());

        assertEquals("Erro: Nenhum medicamento encontrado.", exception.getMessage());
    }

    @Test
    void testFindById_Success() {
        Medicamento medicamento = new Medicamento();
        medicamento.setId(1L);
        medicamento.setNome("Paracetamol");

        when(medicamentoRepository.findById(1L)).thenReturn(Optional.of(medicamento));

        Medicamento result = medicamentoService.findById(1L);

        assertNotNull(result);
        assertEquals("Paracetamol", result.getNome());
        verify(medicamentoRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(medicamentoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> medicamentoService.findById(1L));

        assertEquals("Erro: Medicamento com ID 1 não encontrado.", exception.getMessage());
    }

    @Test
    void testUpdateMedicamento_Success() {
        Medicamento existingMedicamento = new Medicamento();
        existingMedicamento.setId(1L);
        existingMedicamento.setNome("Paracetamol");

        Medicamento updatedDetails = new Medicamento();
        updatedDetails.setNome("Paracetamol Atualizado");
        updatedDetails.setDescricao("Analgésico Atualizado");
        updatedDetails.setEndereco("Rua B");

        when(medicamentoRepository.findById(1L)).thenReturn(Optional.of(existingMedicamento));
        when(medicamentoRepository.save(any(Medicamento.class))).thenReturn(updatedDetails);

        Medicamento result = medicamentoService.update(1L, updatedDetails);

        assertNotNull(result);
        assertEquals("Paracetamol Atualizado", result.getNome());
        assertEquals("Analgésico Atualizado", result.getDescricao());
        verify(medicamentoRepository, times(1)).save(existingMedicamento);
    }

    @Test
    void testUpdateMedicamento_NotFound() {
        Medicamento updatedDetails = new Medicamento();
        updatedDetails.setNome("Paracetamol Atualizado");

        when(medicamentoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> medicamentoService.update(1L, updatedDetails));

        assertEquals("Erro: Medicamento com ID 1 não encontrado.", exception.getMessage());
    }

    @Test
    void testDeleteMedicamento_Success() {
        when(medicamentoRepository.existsById(1L)).thenReturn(true);

        medicamentoService.delete(1L);

        verify(medicamentoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteMedicamento_NotFound() {
        when(medicamentoRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> medicamentoService.delete(1L));

        assertEquals("Erro: Medicamento com ID 1 não encontrado.", exception.getMessage());
    }
}
