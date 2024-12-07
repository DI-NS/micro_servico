package com.usuario.usuario_microservico.Controller;

import com.usuario.usuario_microservico.Model.Medicamento;
import com.usuario.usuario_microservico.Service.UserService;
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

    // MockMvc permite a simulação de requisições HTTP.
    @Autowired
    private MockMvc mockMvc;

    // MockBean cria um mock do UserService, utilizado pelo UserController.
    @MockBean
    private UserService userService;

    /**
     * Testa se o endpoint "/usuarios/medicamentos" retorna uma lista de medicamentos.
     */
    @Test
    void ReturnListOfMedicamentos() throws Exception {
        // Cria um medicamento de exemplo.
        Medicamento medicamento = new Medicamento();
        medicamento.setNome("Paracetamol");
        medicamento.setDescricao("Analgésico");
        medicamento.setEndereco("Farmácia Central");

        // Lista simulada de medicamentos.
        List<Medicamento> medicamentos = Arrays.asList(medicamento);

        // Simula o comportamento do serviço para retornar a lista.
        when(userService.getAllMedicamentos()).thenReturn(medicamentos);

        // Realiza a chamada ao endpoint e valida o retorno.
        mockMvc.perform(get("/usuarios/medicamentos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Verifica se o status é 200 OK.
                .andExpect(jsonPath("$[0].nome").value("Paracetamol")) // Verifica o nome.
                .andExpect(jsonPath("$[0].descricao").value("Analgésico")) // Verifica a descrição.
                .andExpect(jsonPath("$[0].endereco").value("Farmácia Central")); // Verifica o endereço.
    }

    /**
     * Testa se o endpoint "/usuarios/medicamentos/{id}" retorna um medicamento pelo ID.
     */
    @Test
    void ReturnMedicamentoById() throws Exception {
        // Cria um medicamento de exemplo.
        Medicamento medicamento = new Medicamento();
        medicamento.setNome("Ibuprofeno");
        medicamento.setDescricao("Anti-inflamatório");
        medicamento.setEndereco("Farmácia Central");

        // Simula o comportamento do serviço para retornar o medicamento.
        when(userService.getMedicamentoById(1L)).thenReturn(medicamento);

        // Realiza a chamada ao endpoint e valida o retorno.
        mockMvc.perform(get("/usuarios/medicamentos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Verifica se o status é 200 OK.
                .andExpect(jsonPath("$.nome").value("Ibuprofeno")) // Verifica o nome.
                .andExpect(jsonPath("$.descricao").value("Anti-inflamatório")) // Verifica a descrição.
                .andExpect(jsonPath("$.endereco").value("Farmácia Central")); // Verifica o endereço.
    }

    /**
     * Testa se o endpoint "/usuarios/medicamentos" retorna 500 quando o serviço falha.
     */
    @Test
    void Return500WhenServiceFailsToGetAllMedicamentos() throws Exception {
        // Simula uma exceção no serviço.
        when(userService.getAllMedicamentos()).thenThrow(new RuntimeException("Unexpected error"));

        // Realiza a chamada ao endpoint e valida o retorno.
        mockMvc.perform(get("/usuarios/medicamentos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError()) // Verifica se o status é 500.
                .andExpect(jsonPath("$.message").value("Unexpected error")); // Verifica a mensagem de erro.
    }

    /**
     * Testa se o endpoint "/usuarios/medicamentos/{id}" retorna 404 quando o medicamento não é encontrado.
     */
    @Test
    void Return404WhenMedicamentoNotFoundById() throws Exception {
        // Simula exceção quando um ID inexistente é solicitado.
        when(userService.getMedicamentoById(999L)).thenThrow(new NoSuchElementException("Medicamento not found"));

        // Realiza a chamada ao endpoint e valida o retorno.
        mockMvc.perform(get("/usuarios/medicamentos/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) // Verifica se o status é 404.
                .andExpect(jsonPath("$.message").value("Medicamento not found")); // Verifica a mensagem de erro.
    }
}
