package com.purrfect.adoption_service.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException e) {
        logger.error("User not found: {}", e.getMessage());
        Map<String, Object> error = new HashMap<>();
        error.put("error", "User not found");
        error.put("message", e.getMessage());
        error.put("status", HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(PetNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePetNotFoundException(PetNotFoundException e) {
        logger.error("Pet not found: {}", e.getMessage());
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Pet not found");
        error.put("message", e.getMessage());
        error.put("status", HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(PetNotAvailableException.class)
    public ResponseEntity<Map<String, Object>> handlePetNotAvailableException(PetNotAvailableException e) {
        logger.error("Pet not available: {}", e.getMessage());
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Pet not available");
        error.put("message", e.getMessage());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<Map<String, Object>> handleExternalServiceException(ExternalServiceException e) {
        logger.error("External service error: {}", e.getMessage(), e);
        Map<String, Object> error = new HashMap<>();
        error.put("error", "External service unavailable");
        error.put("message", e.getMessage());
        error.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {
        logger.error("Runtime error: {}", e.getMessage(), e);
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Internal server error");
        error.put("message", e.getMessage());
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}


