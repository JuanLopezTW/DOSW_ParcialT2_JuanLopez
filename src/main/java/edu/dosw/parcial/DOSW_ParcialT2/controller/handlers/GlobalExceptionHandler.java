package edu.dosw.parcial.DOSW_ParcialT2.controller.handlers;

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