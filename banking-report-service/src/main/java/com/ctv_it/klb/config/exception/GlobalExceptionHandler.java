package com.ctv_it.klb.config.exception;

import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.response.ErrorDetailDTO;
import com.ctv_it.klb.dto.response.ErrorResponseDTO;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.BadRequestException;
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
  public ResponseEntity<ErrorResponseDTO> handleNotFoundException(NotFoundExceptionCustomize ex,
      HttpServletRequest request) {
    log.error("Not found exception: {}", ex.getErrors());
    return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), ex.getErrors(), request);
  }

  @ExceptionHandler(ConflictExceptionCustomize.class)
  public ResponseEntity<ErrorResponseDTO> handleConflictException(ConflictExceptionCustomize ex,
      HttpServletRequest request) {
    log.error("Conflict exception: {}", ex.getErrors());
    return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), ex.getErrors(), request);
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
    return buildErrorResponse(HttpStatus.BAD_REQUEST, Translator.toLocale("error.invalid.data"),
        errors, request);
  }

  @ExceptionHandler(InvalidExceptionCustomize.class)
  public ResponseEntity<ErrorResponseDTO> handleInvalidException(InvalidExceptionCustomize ex,
      HttpServletRequest request) {
    log.error("Invalid exception: {}", ex.getErrors());
    return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getErrors(), request);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponseDTO> handleBadRequestException(BadRequestException ex,
      HttpServletRequest request) {
    log.error("Bad request exception: {}", ex.getMessage());
    return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null, request);
  }

  @ExceptionHandler({InternalServerError.class, InternalError.class, Exception.class})
  public ResponseEntity<ErrorResponseDTO> handleInternalError(Exception ex,
      HttpServletRequest request) {
    log.error("Internal server error: {}", ex.getMessage(), ex);
    return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
        Translator.toLocale("error.internal-server"), null, request);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex, HttpServletRequest request) {
    ErrorDetailDTO errorDetail = extractErrorDetails(ex);
    return buildErrorResponse(HttpStatus.BAD_REQUEST, Translator.toLocale("error.invalid.data"),
        Collections.singletonList(errorDetail), request);
  }

  @ExceptionHandler(UnrecognizedPropertyException.class)
  public ResponseEntity<ErrorResponseDTO> handleUnrecognizedPropertyException(
      UnrecognizedPropertyException ex, HttpServletRequest request) {

    // Extract the unrecognized field's name
    String fieldName = ex.getPropertyName();

    // Extract the rejected value if available
    JsonParser parser = (JsonParser) ex.getProcessor();
    String rejectedValue = null;
    if (parser != null && parser.getCurrentToken() != null) {
      rejectedValue = parser.getCurrentToken().toString();
    }

    // Construct a detailed message
    String message = String.format(
        Translator.toLocale("error.invalid.unrecognized-2", fieldName, rejectedValue));

    // Create an error detail object
    ErrorDetailDTO errorDetail = createErrorDetail(fieldName, message);

    // Return a well-formatted error response
    return buildErrorResponse(
        HttpStatus.BAD_REQUEST,
        Translator.toLocale("error.invalid.data"),
        Collections.singletonList(errorDetail),
        request
    );
  }


  @ExceptionHandler(JsonProcessingException.class)
  public ResponseEntity<ErrorResponseDTO> handleJsonProcessingException(JsonProcessingException ex,
      HttpServletRequest request) {
    log.error("JSON processing exception: {}", ex.getMessage());
    ErrorDetailDTO errorDetail = createErrorDetail("json",
        Translator.toLocale("error.invalid.json"));
    return buildErrorResponse(HttpStatus.BAD_REQUEST, Translator.toLocale("error.invalid.data"),
        Collections.singletonList(errorDetail), request);
  }

  @ExceptionHandler(JsonMappingException.class)
  public ResponseEntity<ErrorResponseDTO> handleJsonMappingException(JsonMappingException ex,
      HttpServletRequest request) {
    List<ErrorDetailDTO> errors = ex.getPath().stream()
        .map(ref -> createErrorDetail(ref.getFieldName(),
            "Error mapping field '" + ref.getFieldName() + "'"))
        .collect(Collectors.toList());
    return buildErrorResponse(HttpStatus.BAD_REQUEST, Translator.toLocale("error.invalid.data"),
        errors, request);
  }

  // Helper method to create an error response
  private ResponseEntity<ErrorResponseDTO> buildErrorResponse(HttpStatus status, String message,
      List<ErrorDetailDTO> errors, HttpServletRequest request) {
    ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
        .success(false)
        .message(message)
        .url(request.getServletPath())
        .status(status.value())
        .errors(errors)
        .build();
    log.error("Error occurred at URL {}: {}", request.getServletPath(), errors);
    return ResponseEntity.status(status).body(errorResponse);
  }

  // Helper method to create ErrorDetailDTO
  private ErrorDetailDTO createErrorDetail(String field, String message) {
    return ErrorDetailDTO.builder()
        .field(field)
        .message(message)
        .build();
  }

  // Extract error details from HttpMessageNotReadableException
  private ErrorDetailDTO extractErrorDetails(HttpMessageNotReadableException ex) {
    if (ex.getCause() instanceof InvalidFormatException ife) {
      return handleInvalidFormatException(ife);
    } else if (ex.getCause() instanceof MismatchedInputException mie) {
      return handleMismatchedInputException(mie);
    }
    return createErrorDetail("request", "Malformed JSON request");
  }

  private ErrorDetailDTO handleInvalidFormatException(InvalidFormatException ife) {
    // Extract the field name
    String field = ife.getPath().stream()
        .map(Reference::getFieldName)
        .findFirst()
        .orElse("unknown");

    String rejectedValue = ife.getValue() != null ? ife.getValue().toString() : "null";
    String targetType =
        ife.getTargetType() != null ? ife.getTargetType().getSimpleName() : "unknown";

    String message = String.format("Cannot convert value '%s' to %s", rejectedValue, targetType);
    log.error("InvalidFormatException: Field: {}, Rejected Value: {}, Target Type: {}, Message: {}",
        field, rejectedValue, targetType, message);

    return createErrorDetail(field, message);
  }


  private ErrorDetailDTO handleMismatchedInputException(MismatchedInputException mie) {
    String field = mie.getPath().stream().map(Reference::getFieldName).findFirst()
        .orElse("unknown");
    String message =
        "Malformed JSON request. " + (mie.getMessage() != null ? mie.getMessage() : "");
    log.error("MismatchedInputException: Field: {}, Message: {}", field, message);
    return createErrorDetail(field, message);
  }
}
