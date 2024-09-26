package com.app.bankingloanservice.exception;

import com.app.bankingloanservice.dto.ApiResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LoanApplicationNotFoundException.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleLoanApplicationNotFoundException(LoanApplicationNotFoundException ex) {
        // Log the exception with relevant information
        log.error("Loan application not found: {}", ex.getMessage(), ex);

        ApiResponseWrapper<String> response = new ApiResponseWrapper<>(
                HttpStatus.NOT_FOUND.value(),
                false,
                "Loan application not found: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleDocumentNotFoundException(DocumentNotFoundException ex) {
        // Log the document not found error
        log.error("Document not found: {}", ex.getMessage(), ex);

        ApiResponseWrapper<String> response = new ApiResponseWrapper<>(
                HttpStatus.NOT_FOUND.value(),
                false,
                "Document not found: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidDocumentException.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleInvalidDocumentException(InvalidDocumentException ex) {
        // Log the invalid document error
        log.error("Invalid document: {}", ex.getMessage(), ex);

        ApiResponseWrapper<String> response = new ApiResponseWrapper<>(
                HttpStatus.BAD_REQUEST.value(),
                false,
                "Invalid document: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCollateralException.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleInvalidCollateralException(InvalidCollateralException ex) {
        // Log the invalid collateral error
        log.error("Invalid collateral: {}", ex.getMessage(), ex);

        ApiResponseWrapper<String> response = new ApiResponseWrapper<>(
                HttpStatus.BAD_REQUEST.value(),
                false,
                "Invalid collateral: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CollateralNotFoundException.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleCollateralNotFoundException(CollateralNotFoundException ex) {
        // Log the collateral not found error
        log.error("Collateral not found: {}", ex.getMessage(), ex);

        ApiResponseWrapper<String> response = new ApiResponseWrapper<>(
                HttpStatus.NOT_FOUND.value(),
                false,
                "Collateral not found: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        // Log the illegal argument error
        log.error("Invalid input provided: {}", ex.getMessage(), ex);

        ApiResponseWrapper<String> response = new ApiResponseWrapper<>(
                HttpStatus.BAD_REQUEST.value(),
                false,
                "Invalid input: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleFileStorageException(FileStorageException ex) {
        // Log the file storage error
        log.error("File storage error: {}", ex.getMessage(), ex);

        ApiResponseWrapper<String> response = new ApiResponseWrapper<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                false,
                "File storage error: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleGeneralException(Exception ex) {
        // Log any other unexpected exceptions
        log.error("An unexpected error occurred: {}", ex.getMessage(), ex);

        ApiResponseWrapper<String> response = new ApiResponseWrapper<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                false,
                "An unexpected error occurred: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
