package com.usuario.usuario_microservico.Controller;

import com.usuario.usuario_microservico.Model.Medicamento;
import com.usuario.usuario_microservico.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private Medicamento medicamento;

    @BeforeEach
    void setUp() {
        medicamento = new Medicamento();
        medicamento.setNome("Paracetamol");
        medicamento.setDescricao("Analgésico");
        medicamento.setEndereco("Farmácia Central");
    }

    @Test
    void testGetAllMedicamentos() throws Exception {
        List<Medicamento> medicamentos = Arrays.asList(medicamento);

        when(userService.getAllMedicamentos()).thenReturn(medicamentos);

        mockMvc.perform(get("/usuarios/medicamentos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Paracetamol"))
                .andExpect(jsonPath("$[0].descricao").value("Analgésico"))
                .andExpect(jsonPath("$[0].endereco").value("Farmácia Central"));
    }

    @Test
    void testGetMedicamentoById() throws Exception {
        when(userService.getMedicamentoById(1L)).thenReturn(medicamento);

        mockMvc.perform(get("/usuarios/medicamentos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Paracetamol"))
                .andExpect(jsonPath("$.descricao").value("Analgésico"))
                .andExpect(jsonPath("$.endereco").value("Farmácia Central"));
    }

    @Test
    void testGetAllMedicamentosThrowsException() throws Exception {
        when(userService.getAllMedicamentos()).thenThrow(new RuntimeException("Erro ao buscar medicamentos"));

        mockMvc.perform(get("/usuarios/medicamentos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Erro ao buscar medicamentos"));
    }

    @Test
    void testGetMedicamentoByIdNotFound() throws Exception {
        when(userService.getMedicamentoById(99L)).thenThrow(new NoSuchElementException("Medicamento não encontrado"));

        mockMvc.perform(get("/usuarios/medicamentos/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Medicamento não encontrado"));
    }
}
