package com.ctv_it.klb.config.exception;

import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.response.ErrorDetailDTO;
import com.ctv_it.klb.dto.response.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

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
            .errors(ex.getErrors())
            .build());
  }

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
            .errors(ex.getErrors())
            .build());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex, HttpServletRequest request) {

    List<ErrorDetailDTO> errors = ex.getBindingResult().getFieldErrors().stream()
        .map(fieldError -> ErrorDetailDTO.builder()
            .field(fieldError.getField())
            .rejectedValue(fieldError.getRejectedValue())
            .message(Translator.toLocale(fieldError.getDefaultMessage()))
            .build())
        .collect(Collectors.toList());

    ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
        .status(getErrorStatus())
        .message(Translator.toLocale("error.invalid.data"))
        .url(request.getServletPath())
        .code(HttpStatus.BAD_REQUEST.value())
        .errors(errors)
        .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
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
            .errors(ex.getErrors())
            .build());
  }

  @ExceptionHandler(InternalError.class)
  public ResponseEntity<ErrorResponseDTO> handleInternalError(
      InternalError ex, HttpServletRequest request) {

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ErrorResponseDTO.builder()
            .status(getErrorStatus())
            .message(ex.getMessage())
            .url(request.getServletPath())
            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .errors(null)
            .build());
  }

  private String getErrorStatus() {
    return Translator.toLocale("status.unsuccessfully");
  }
}
