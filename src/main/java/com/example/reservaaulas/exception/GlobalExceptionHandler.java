package com.example.reservaaulas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Maneja errores de validación (campos mal rellenados)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errores = new HashMap<>();

        // Recoge todos los errores de validación
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String campo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            errores.put(campo, mensaje);
        });

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("mensaje", "Error de validación");
        respuesta.put("errores", errores);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    //Maneja cuando no se encuentra un recurso
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(
            ResourceNotFoundException ex) {

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("mensaje", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }

    //Maneja peticiones incorrectas
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(
            BadRequestException ex) {

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("mensaje", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    //Maneja errores de autenticación
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorized(
            UnauthorizedException ex) {

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("mensaje", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
    }

    //Maneja errores de permisos
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String, Object>> handleForbidden(
            ForbiddenException ex) {

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("mensaje", ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuesta);
    }

    //Maneja cualquier otro error no contemplado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(
            Exception ex) {

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("mensaje", "Error interno del servidor");
        respuesta.put("detalle", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }
}