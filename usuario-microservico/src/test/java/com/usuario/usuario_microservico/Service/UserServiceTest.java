package com.usuario.usuario_microservico.Service;

import com.usuario.usuario_microservico.MedicamentoFeignClient;
import com.usuario.usuario_microservico.Model.Medicamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


public class UserServiceTest {

    // Injeta um mock do UserService para ser testado.
    @InjectMocks
    private UserService userService;

    // Cria um mock do MedicamentoFeignClient, usado pelo UserService.
    @Mock
    private MedicamentoFeignClient feignClient;

    //Método executado antes de cada teste para inicializar os mocks.
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Teste para verificar se o método getAllMedicamentos retorna todos os medicamentos corretamente.
     */
    @Test
    void ReturnAllMedicamentos() {
        // Criação de um medicamento de exemplo.
        Medicamento medicamento = new Medicamento();
        medicamento.setNome("Paracetamol");
        medicamento.setDescricao("Analgésico");
        medicamento.setEndereco("Farmácia Central");

        // Simula o retorno do FeignClient com uma lista contendo o medicamento.
        List<Medicamento> medicamentos = Arrays.asList(medicamento);
        when(feignClient.getAllMedicamentos()).thenReturn(medicamentos);

        // Chama o método que está sendo testado.
        List<Medicamento> result = userService.getAllMedicamentos();

        // Verifica se o tamanho da lista retornada é 1 e se os atributos correspondem.
        assertEquals(1, result.size());
        assertEquals("Paracetamol", result.get(0).getNome());
    }

    /**
     * Teste para verificar se o método getMedicamentoById retorna o medicamento correto com base no ID.
     */
    @Test
    void ReturnMedicamentoById() {
        // Criação de um medicamento de exemplo.
        Medicamento medicamento = new Medicamento();
        medicamento.setNome("Ibuprofeno");
        medicamento.setDescricao("Anti-inflamatório");
        medicamento.setEndereco("Farmácia Central");

        // Simula o retorno do FeignClient com o medicamento baseado no ID.
        when(feignClient.getMedicamentoById(1L)).thenReturn(medicamento);

        // Chama o método que está sendo testado.
        Medicamento result = userService.getMedicamentoById(1L);

        // Verifica se os atributos do medicamento retornado correspondem ao esperado.
        assertEquals("Ibuprofeno", result.getNome());
        assertEquals("Anti-inflamatório", result.getDescricao());
    }

    /**
     * Teste para verificar se uma exceção é lançada quando o FeignClient falha ao buscar um medicamento por ID.
     */
    @Test
    void ThrowExceptionWhenFeignClientFailsInGetMedicamentoById() {
        // Simula o lançamento de uma exceção pelo FeignClient.
        when(feignClient.getMedicamentoById(1L)).thenThrow(new RuntimeException("Feign client error"));

        // Verifica se a exceção lançada possui a mensagem esperada.
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getMedicamentoById(1L);
        });

        assertEquals("Feign client error", exception.getMessage());
    }

    /**
     * Teste para verificar se uma exceção é lançada quando o FeignClient falha ao buscar todos os medicamentos.
     */
    @Test
    void ThrowExceptionWhenFeignClientFailsInGetAllMedicamentos() {
        // Simula o lançamento de uma exceção pelo FeignClient.
        when(feignClient.getAllMedicamentos()).thenThrow(new RuntimeException("Feign client error"));

        // Verifica se a exceção lançada possui a mensagem esperada.
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getAllMedicamentos();
        });

        assertEquals("Feign client error", exception.getMessage());
    }
}
