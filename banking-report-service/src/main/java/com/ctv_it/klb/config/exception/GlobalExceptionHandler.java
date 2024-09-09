package com.ctv_it.klb.config.exception;

import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.response.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  // Not Found (404)
  @ExceptionHandler(NotFoundExceptionCustomize.class)
  public ResponseEntity<ErrorResponseDTO> handleNotFoundException(
      NotFoundExceptionCustomize ex, HttpServletRequest request) {

    log.error("Not found exception: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ErrorResponseDTO.builder()
            .status(getErrorStatus())
            .message(ex.getMessage())
            .url(request.getServletPath())
            .code(HttpStatus.NOT_FOUND.value())
            .details(ex.getErrors())
            .build());
  }

  // Conflict (409)
  @ExceptionHandler(ConflictExceptionCustomize.class)
  public ResponseEntity<ErrorResponseDTO> handleConflictException(
      ConflictExceptionCustomize ex, HttpServletRequest request) {
    log.error("Conflict exception: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(ErrorResponseDTO.builder()
            .status(getErrorStatus())
            .message(ex.getMessage())
            .url(request.getServletPath())
            .code(HttpStatus.CONFLICT.value())
            .details(ex.getErrors())
            .build());
  }

  // BadRequest (400)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex, HttpServletRequest request) {

    Map<String, Object> errors = new HashMap<>();

    for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
      String field = fieldError.getField();
      String defaultMessage = fieldError.getDefaultMessage();

      // Resolve the default message to the localized message
      String localizedMessage = Translator.toLocale(defaultMessage);

      // Add the localized message to errors map
      errors.put(field, localizedMessage);
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ErrorResponseDTO.builder()
            .status(getErrorStatus())
            .message(Translator.toLocale("error.invalid.data"))
            .url(request.getServletPath())
            .code(HttpStatus.BAD_REQUEST.value())
            .details(errors)
            .build());
  }

  @ExceptionHandler(InvalidExceptionCustomize.class)
  public ResponseEntity<ErrorResponseDTO> handleInvalidException(
      InvalidExceptionCustomize ex, HttpServletRequest request) {

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ErrorResponseDTO.builder()
            .status(getErrorStatus())
            .message(ex.getMessage())
            .url(request.getServletPath())
            .code(HttpStatus.BAD_REQUEST.value())
            .details(ex.getErrors())
            .build());
  }

  private String getErrorStatus() {
    return Translator.toLocale("status.unsuccessfully");
  }
}
