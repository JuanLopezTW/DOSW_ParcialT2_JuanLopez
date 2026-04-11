package edu.dosw.parcial.DOSW_ParcialT2.controller.handlers;

import edu.dosw.parcial.DOSW_ParcialT2.core.exceptions.ActiveOrderAlreadyExistsException;
import edu.dosw.parcial.DOSW_ParcialT2.core.exceptions.InvalidOrderStatusException;
import edu.dosw.parcial.DOSW_ParcialT2.core.exceptions.OrderNotFoundException;
import edu.dosw.parcial.DOSW_ParcialT2.core.exceptions.ProductNotFoundException;
import edu.dosw.parcial.DOSW_ParcialT2.core.exceptions.ProductUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound(ProductNotFoundException e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ProductUnavailableException.class)
    public ResponseEntity<Map<String, String>> handleProductUnavailable(ProductUnavailableException e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ActiveOrderAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleActiveOrderAlreadyExists(ActiveOrderAlreadyExistsException e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleOrderNotFound(OrderNotFoundException e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidOrderStatusException.class)
    public ResponseEntity<Map<String, String>> handleInvalidOrderStatus(InvalidOrderStatusException e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException e) {
        log.warn("error de validación de input: {}", e.getMessage());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Los datos ingresados no son válidos");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        String message = e.getMessage();

        if (message.equals("El correo ya se encuentra registrado")) {
            log.warn("conflicto: {}", message);
            Map<String, String> response = new HashMap<>();
            response.put("message", message);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        if (message.equals("Credenciales inválidas")) {
            log.warn("autenticación fallida: {}", message);
            Map<String, String> response = new HashMap<>();
            response.put("message", message);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        log.error("error interno del servidor: {}", message);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Error interno del servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}