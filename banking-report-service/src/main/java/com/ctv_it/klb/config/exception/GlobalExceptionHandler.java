package com.ctv_it.klb.config.exception;

import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.response.ErrorDetailDTO;
import com.ctv_it.klb.dto.response.ErrorResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.BadRequestException;
import java.rmi.ServerError;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundExceptionCustomize.class)
  public ResponseEntity<ErrorResponseDTO> handleNotFoundException(
      NotFoundExceptionCustomize ex, HttpServletRequest request) {

    log.error("Not found exception: {}", ex.getErrors());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ErrorResponseDTO.builder()
            .success(Boolean.FALSE)
            .message(ex.getMessage())
            .url(request.getServletPath())
            .status(HttpStatus.NOT_FOUND.value())
            .errors(ex.getErrors())
            .build());
  }

  @ExceptionHandler(ConflictExceptionCustomize.class)
  public ResponseEntity<ErrorResponseDTO> handleConflictException(
      ConflictExceptionCustomize ex, HttpServletRequest request) {

    log.error("Conflict exception: {}", ex.getErrors());
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(ErrorResponseDTO.builder()
            .success(Boolean.FALSE)
            .message(ex.getMessage())
            .url(request.getServletPath())
            .status(HttpStatus.CONFLICT.value())
            .errors(ex.getErrors())
            .build());
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
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
        .success(Boolean.FALSE)
        .message(Translator.toLocale("error.invalid.data"))
        .url(request.getServletPath())
        .status(HttpStatus.BAD_REQUEST.value())
        .errors(errors)
        .build();

    log.error("Bad request exception: {}", errors);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(InvalidExceptionCustomize.class)
  public ResponseEntity<ErrorResponseDTO> handleInvalidException(
      InvalidExceptionCustomize ex, HttpServletRequest request) {

    log.error("Invalid exception: {}", ex.getErrors());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ErrorResponseDTO.builder()
            .success(Boolean.FALSE)
            .message(ex.getMessage())
            .url(request.getServletPath())
            .status(HttpStatus.BAD_REQUEST.value())
            .errors(ex.getErrors())
            .build());
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponseDTO> handleBadRequestException(
      BadRequestException ex, HttpServletRequest request) {

    log.error("Bad request exception: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ErrorResponseDTO.builder()
            .success(Boolean.FALSE)
            .message(ex.getMessage())
            .url(request.getServletPath())
            .status(HttpStatus.BAD_REQUEST.value())
            .build());
  }

  @ExceptionHandler({InternalServerError.class, InternalError.class, ServerError.class})
  public ResponseEntity<ErrorResponseDTO> handleInternalError(
      Exception ex, HttpServletRequest request) {

    log.error("InternalError exception: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ErrorResponseDTO.builder()
            .success(Boolean.FALSE)
            .message(Translator.toLocale("error.internal-server"))
            .url(request.getServletPath())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .build());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex, HttpServletRequest request) {

    ErrorDetailDTO errorDetail = extractErrorDetails(ex);

    ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
        .success(Boolean.FALSE)
        .message(Translator.toLocale("error.invalid.data"))
        .url(request.getServletPath())
        .status(HttpStatus.BAD_REQUEST.value())
        .errors(Collections.singletonList(errorDetail))
        .build();

    // Log the exception
    log.error("Malformed JSON request: {}", ex.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(UnrecognizedPropertyException.class)
  public ResponseEntity<ErrorResponseDTO> handleUnrecognizedPropertyException(
      UnrecognizedPropertyException ex, HttpServletRequest request) {

    String unrecognizedField = ex.getPropertyName();
    String message = String.format("Unrecognized field '%s' in request", unrecognizedField);

    ErrorDetailDTO errorDetail = ErrorDetailDTO.builder()
        .field(unrecognizedField)
        .message(message)
        .build();

    ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
        .success(Boolean.FALSE)
        .message(Translator.toLocale("error.invalid.data"))
        .url(request.getServletPath())
        .status(HttpStatus.BAD_REQUEST.value())
        .errors(Collections.singletonList(errorDetail))
        .build();

    log.error("Unrecognized field exception: {}", message);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  private ErrorDetailDTO extractErrorDetails(HttpMessageNotReadableException ex) {
    ErrorDetailDTO errorDetail;

    if (ex.getCause() instanceof InvalidFormatException ife) {
      String field = ife.getPath().stream()
          .map(Reference::getFieldName)
          .reduce((first, second) -> second)
          .orElse("unknown");

      String rejectedValue = ife.getValue() != null ? ife.getValue().toString() : null;
      String targetType =
          ife.getTargetType() != null ? ife.getTargetType().getSimpleName() : "unknown";

      String message = String.format("Cannot convert value '%s' to %s", rejectedValue, targetType);

      errorDetail = ErrorDetailDTO.builder()
          .field(field)
          .rejectedValue(rejectedValue)
          .message(message)
          .build();

      log.error(
          "InvalidFormatException: Field: {}, Rejected Value: {}, Target Type: {}, Message: {}",
          field, rejectedValue, targetType, message);
    } else if (ex.getCause() instanceof MismatchedInputException mie) {
      String field = mie.getPath().stream()
          .map(Reference::getFieldName)
          .reduce((first, second) -> second)
          .orElse("unknown");

      String message = "Malformed JSON request. ";

      if (mie.getMessage() != null) {
        message += mie.getMessage();
      }

      errorDetail = ErrorDetailDTO.builder()
          .field(field)
          .message(message)
          .build();

      log.error("MismatchedInputException: Field: {}, Message: {}", field, message);
    } else {
      errorDetail = ErrorDetailDTO.builder()
          .field("request")
          .message("Malformed JSON request")
          .build();

      log.error("Malformed JSON request: {}", "Malformed JSON request");
      log.error("HttpMessageNotReadableException: {}", ex.getMessage(), ex);
    }

    return errorDetail;
  }

  @ExceptionHandler(JsonProcessingException.class)
  public ResponseEntity<ErrorResponseDTO> handleJsonProcessingException(
      JsonProcessingException ex, HttpServletRequest request) {

    log.error("JSON processing exception: {}", ex.getMessage());

    ErrorDetailDTO errorDetail = ErrorDetailDTO.builder()
        .field("json")
        .message(Translator.toLocale("error.invalid.json"))
        .build();

    ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
        .success(Boolean.FALSE)
        .message(Translator.toLocale("error.invalid.data"))
        .url(request.getServletPath())
        .status(HttpStatus.BAD_REQUEST.value())
        .errors(Collections.singletonList(errorDetail))
        .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(JsonMappingException.class)
  public ResponseEntity<ErrorResponseDTO> handleJsonMappingException(
      JsonMappingException ex, HttpServletRequest request) {

    List<ErrorDetailDTO> errors = ex.getPath().stream()
        .map(ref -> ErrorDetailDTO.builder()
            .field(ref.getFieldName())
            .message("Error mapping field " + ref.getFieldName())
            .build())
        .collect(Collectors.toList());

    ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
        .success(Boolean.FALSE)
        .message(Translator.toLocale("error.invalid.data"))
        .url(request.getServletPath())
        .status(HttpStatus.BAD_REQUEST.value())
        .errors(errors)
        .build();

    log.error("JsonMappingException: {}", errors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

}
