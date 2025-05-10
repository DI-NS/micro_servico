package com.medicamento.medicamento_microservico.controller;

import com.medicamento.medicamento_microservico.model.Medicamento;
import com.medicamento.medicamento_microservico.service.MedicamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NO_CONTENT;

class MedicamentoControllerTest {

    @InjectMocks
    private MedicamentoController medicamentoController;

    @Mock
    private MedicamentoService medicamentoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMedicamento() {
        Medicamento medicamento = new Medicamento();
        medicamento.setNome("Ibuprofeno");
        medicamento.setDescricao("Analgésico");
        medicamento.setEndereco("Rua A");

        when(medicamentoService.create(medicamento)).thenReturn(medicamento);

        Medicamento result = medicamentoController.createMedicamento(medicamento);

        assertNotNull(result);
        assertEquals("Ibuprofeno", result.getNome());
        verify(medicamentoService, times(1)).create(medicamento);
    }

    @Test
    void testGetAllMedicamento() {
        Medicamento medicamento1 = new Medicamento();
        medicamento1.setNome("Ibuprofeno");

        Medicamento medicamento2 = new Medicamento();
        medicamento2.setNome("Paracetamol");

        when(medicamentoService.findAll()).thenReturn(Arrays.asList(medicamento1, medicamento2));

        List<Medicamento> medicamentos = medicamentoController.getAllMedicamento();

        assertEquals(2, medicamentos.size());
        assertEquals("Ibuprofeno", medicamentos.get(0).getNome());
        assertEquals("Paracetamol", medicamentos.get(1).getNome());
        verify(medicamentoService, times(1)).findAll();
    }

    @Test
    void testGetMedicamentoById() {
        Medicamento medicamento = new Medicamento();
        medicamento.setId(1L);
        medicamento.setNome("Ibuprofeno");

        when(medicamentoService.findById(1L)).thenReturn(medicamento);

        Medicamento result = medicamentoController.getMedicamentoById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Ibuprofeno", result.getNome());
        verify(medicamentoService, times(1)).findById(1L);
    }

    @Test
    void testUpdateMedicamento() {
        Medicamento medicamento = new Medicamento();
        medicamento.setNome("Ibuprofeno Atualizado");
        medicamento.setDescricao("Analgésico Atualizado");
        medicamento.setEndereco("Rua B");

        when(medicamentoService.update(eq(1L), any(Medicamento.class))).thenReturn(medicamento);

        Medicamento result = medicamentoController.updateMedicamento(1L, medicamento);

        assertNotNull(result);
        assertEquals("Ibuprofeno Atualizado", result.getNome());
        assertEquals("Analgésico Atualizado", result.getDescricao());
        assertEquals("Rua B", result.getEndereco());
        verify(medicamentoService, times(1)).update(1L, medicamento);
    }

    @Test
    void testDeleteMedicamento() {
        ResponseEntity<Void> response = medicamentoController.deleteMedicamento(1L);

        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(medicamentoService, times(1)).delete(1L);
    }
}
