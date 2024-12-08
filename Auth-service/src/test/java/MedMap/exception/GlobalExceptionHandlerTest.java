package MedMap.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @Mock
    private HttpServletRequest mockRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleUserAlreadyExistsExceptionForSwaggerRequest() {
        // Arrange
        when(mockRequest.getRequestURI()).thenReturn("/v3/api-docs");

        UserAlreadyExistsException exception = new UserAlreadyExistsException("User already exists");

        // Act
        ResponseEntity<String> response = exceptionHandler.handleUserAlreadyExists(exception, mockRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Exceção ignorada na documentação Swagger", response.getBody());
        verify(mockRequest, times(1)).getRequestURI();
    }

    @Test
    void testHandleUserAlreadyExistsExceptionForNonSwaggerRequest() {
        // Arrange
        when(mockRequest.getRequestURI()).thenReturn("/api/users");

        UserAlreadyExistsException exception = new UserAlreadyExistsException("User already exists");

        // Act
        ResponseEntity<String> response = exceptionHandler.handleUserAlreadyExists(exception, mockRequest);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User already exists", response.getBody());
        verify(mockRequest, times(1)).getRequestURI();
    }

    @Test
    void testHandleInvalidCredentialsExceptionForSwaggerRequest() {
        // Arrange
        when(mockRequest.getRequestURI()).thenReturn("/swagger-ui");

        InvalidCredentialsException exception = new InvalidCredentialsException("Invalid credentials");

        // Act
        ResponseEntity<String> response = exceptionHandler.handleInvalidCredentials(exception, mockRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Exceção ignorada na documentação Swagger", response.getBody());
        verify(mockRequest, times(1)).getRequestURI();
    }

    @Test
    void testHandleInvalidCredentialsExceptionForNonSwaggerRequest() {
        // Arrange
        when(mockRequest.getRequestURI()).thenReturn("/api/auth");

        InvalidCredentialsException exception = new InvalidCredentialsException("Invalid credentials");

        // Act
        ResponseEntity<String> response = exceptionHandler.handleInvalidCredentials(exception, mockRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
        verify(mockRequest, times(1)).getRequestURI();
    }
}
