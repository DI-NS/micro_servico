package com.medicamento.medicamento_microservico.Config;

import com.medicamento.medicamento_microservico.service.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private WebApplicationContext context;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        jwtAuthenticationFilter = Mockito.mock(JwtAuthenticationFilter.class); // Mock do filtro
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .addFilters(jwtAuthenticationFilter) // Adicionar o mock manualmente
                .build();
    }

    @Test
    void testSwaggerUIAccessWithoutAuth() throws Exception {
        // Swagger UI deve estar acessível sem autenticação
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }

    @Test
    void testPublicMedicamentoRoutesWithoutAuth() throws Exception {
        // Rotas GET públicas para Medicamento devem estar acessíveis sem autenticação
        mockMvc.perform(get("/medicamento"))
                .andExpect(status().isOk());
    }


    @Test
    void testAuthenticatedRoutesWithoutAuth() throws Exception {
        // Rotas que exigem autenticação devem retornar 401 se não houver autenticação
        mockMvc.perform(post("/medicamento"))
                .andExpect(status().isUnauthorized());
    }
}
