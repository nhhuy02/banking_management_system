package com.ojt.klb.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(PhoneNumberAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handlePhoneNumberAlreadyExistsException(PhoneNumberAlreadyExistsException ex) {
        logger.error("Phone number already exists: {}", ex.getMessage());

        Map<String, Object> body = new HashMap<>();
        body.put("error", "Phone number already exists");
        body.put("details", ex.getMessage());
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("Illegal argument exception: {}", ex.getMessage());
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getMessage());
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.error("Validation failed: {}", ex.getBindingResult().getAllErrors().stream()
                .map(error -> String.format("%s - %s", ((FieldError) error).getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(", ")));

        Map<String, Object> body = new HashMap<>();
        body.put("error", "Validation failed");
        body.put("details", ex.getBindingResult().getAllErrors().stream()
                .map(error -> String.format("%s - %s", ((FieldError) error).getField(), error.getDefaultMessage()))
                .collect(Collectors.toList()));
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex) {
        logger.error("An unexpected error occurred: {}", ex.getMessage(), ex);

        Map<String, Object> body = new HashMap<>();
        body.put("error", "An unexpected error occurred");
        body.put("details", ex.getMessage());
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
