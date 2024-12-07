package MedMap.exception;

import MedMap.model.User;
import MedMap.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import jakarta.servlet.http.HttpServletRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.http.ResponseEntity;



/**
 * Testa o GlobalExceptionHandler, incluindo cenários com Swagger e sem Swagger,
 * e diferentes exceções (UserAlreadyExists e InvalidCredentials).
 */
@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthService authService;

    @Test
    void shouldHandleUserAlreadyExistsException() throws Exception {
        when(authService.register(any(User.class)))
                .thenThrow(new UserAlreadyExistsException("Uma UBS com esse CNES já está registrada."));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomeUbs\":\"UBS Teste\", \"cnes\":\"123456\", \"address\":\"Rua ABC\", \"password\":\"senha\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Uma UBS com esse CNES já está registrada."));
    }

    @Test
    void shouldHandleInvalidCredentialsException() throws Exception {
        when(authService.login("naoencontrado", "qualquer"))
                .thenThrow(new InvalidCredentialsException("UBS não encontrada ou dados inválidos"));

        mockMvc.perform(post("/auth/login")
                        .param("cnes","naoencontrado")
                        .param("password","qualquer"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("UBS não encontrada ou dados inválidos"));
    }

    @Test
    void testUserAlreadyExists_Swagger() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        HttpServletRequest request = new MockHttpServletRequest("GET", "/swagger-ui/index.html");
        ResponseEntity<String> response = handler.handleUserAlreadyExists(
                new UserAlreadyExistsException("Exists"),
                request
        );
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Exceção ignorada na documentação Swagger", response.getBody());
    }

    @Test
    void testUserAlreadyExists_NoSwagger() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        HttpServletRequest request = new MockHttpServletRequest("GET", "/normal-endpoint");
        ResponseEntity<String> response = handler.handleUserAlreadyExists(
                new UserAlreadyExistsException("Exists"),
                request
        );
        assertEquals(409, response.getStatusCodeValue());
        assertEquals("Exists", response.getBody());
    }

    @Test
    void testInvalidCredentials_Swagger() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        HttpServletRequest request = new MockHttpServletRequest("GET", "/v3/api-docs/something");
        ResponseEntity<String> response = handler.handleInvalidCredentials(
                new InvalidCredentialsException("Invalid"),
                request
        );
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Exceção ignorada na documentação Swagger", response.getBody());
    }

    @Test
    void testInvalidCredentials_NoSwagger() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        HttpServletRequest request = new MockHttpServletRequest("GET", "/another-endpoint");
        ResponseEntity<String> response = handler.handleInvalidCredentials(
                new InvalidCredentialsException("Invalid"),
                request
        );
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid", response.getBody());
    }
}
