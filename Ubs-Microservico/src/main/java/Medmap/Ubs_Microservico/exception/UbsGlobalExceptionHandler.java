package Medmap.Ubs_Microservico.exception;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class UbsGlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(UbsGlobalExceptionHandler.class);

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

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ResourceAlreadyExistsException ex,
                                                        HttpServletRequest req) {
        logger.info("ResourceAlreadyExists: {}", ex.getMessage());
        ErrorResponse err = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Conflito de dados",
                ex.getMessage(),    // ex: "CNES já cadastrado"
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex,
                                                        HttpServletRequest req) {
        logger.info("ResourceNotFound: {}", ex.getMessage());
        ErrorResponse err = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Não encontrado",
                ex.getMessage(),    // ex: "UBS não encontrada"
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex,
                                                            HttpServletRequest req) {
        logger.warn("AccessDenied: {}", ex.getMessage());
        ErrorResponse err = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "Acesso negado",
                "Você não tem permissão para executar esta operação.",
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeign(FeignException ex,
                                                     HttpServletRequest req) {
        logger.error("FeignException no UBS-service", ex);
        String msg = ex.contentUTF8().isBlank()
                ? "Falha na comunicação com Auth‑service"
                : ex.contentUTF8();
        ErrorResponse err = new ErrorResponse(
                ex.status(),
                "Erro externo",
                msg,
                req.getRequestURI()
        );
        return ResponseEntity.status(ex.status()).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex,
                                                       HttpServletRequest req) {
        logger.error("Unhandled exception", ex);
        ErrorResponse err = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno",
                "Algo deu errado. Por favor, tente novamente mais tarde.",
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }
}
