package MedMap.exception;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class AuthGlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(AuthGlobalExceptionHandler.class);

    // Validação de payloads @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                          HttpServletRequest req) {
        String details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .distinct()
                .reduce((a, b) -> a + "; " + b)
                .orElse("Dados inválidos");
        logger.warn("Validation error: {}", details);
        ErrorResponse err = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Requisição inválida",
                details,
                req.getRequestURI()
        );
        return ResponseEntity.badRequest().body(err);
    }

    // CNES já cadastrado
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleExists(UserAlreadyExistsException ex,
                                                      HttpServletRequest req) {
        logger.info("UserAlreadyExists: {}", ex.getMessage());
        ErrorResponse err = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Conflito de dados",
                "Uma UBS com esse CNES já existe.",
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
    }

    // Credenciais inválidas
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleAuthFail(InvalidCredentialsException ex,
                                                        HttpServletRequest req) {
        logger.info("InvalidCredentials: {}", ex.getMessage());
        ErrorResponse err = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Falha na autenticação",
                "CNES ou senha inválidos.",
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err);
    }

    // Erros do Feign (ex: falha ao registrar no Auth‑service)
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeign(FeignException ex, HttpServletRequest req) {
        logger.error("FeignException no Auth-service", ex);
        String msg = ex.contentUTF8().isBlank()
                ? "Erro de comunicação com serviço externo"
                : ex.contentUTF8();
        ErrorResponse err = new ErrorResponse(
                ex.status(),
                "Erro externo",
                msg,
                req.getRequestURI()
        );
        return ResponseEntity.status(ex.status()).body(err);
    }

    // Qualquer outra exceção
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest req) {
        logger.error("Unhandled exception", ex);
        ErrorResponse err = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno",
                "Ocorreu um erro inesperado. Tente novamente mais tarde.",
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }
}
