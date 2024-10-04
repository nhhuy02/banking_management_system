package com.app.bankingloanservice.exception;

import com.app.bankingloanservice.dto.ApiResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LoanApplicationNotFoundException.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleLoanApplicationNotFoundException(LoanApplicationNotFoundException ex) {
        log.error("Loan application not found: {}", ex.getMessage(), ex);
        ApiResponseWrapper<String> response = new ApiResponseWrapper<>(
                HttpStatus.NOT_FOUND.value(),
                false,
                "Loan application not found: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidLoanApplicationException.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleInvalidLoanApplicationException(InvalidLoanApplicationException ex) {
        log.error("Invalid loan application: {}", ex.getMessage(), ex);
        ApiResponseWrapper<String> response = new ApiResponseWrapper<>(
                HttpStatus.BAD_REQUEST.value(),
                false,
                "Invalid loan application: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleDocumentNotFoundException(DocumentNotFoundException ex) {
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
        log.error("Collateral not found: {}", ex.getMessage(), ex);
        ApiResponseWrapper<String> response = new ApiResponseWrapper<>(
                HttpStatus.NOT_FOUND.value(),
                false,
                "Collateral not found: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LoanTypeNotFoundException.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleLoanTypeNotFoundException(LoanTypeNotFoundException ex) {
        log.error("Loan type not found: {}", ex.getMessage(), ex);
        ApiResponseWrapper<String> response = new ApiResponseWrapper<>(
                HttpStatus.NOT_FOUND.value(),
                false,
                "Loan type not found: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LoanNotFoundException.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleLoanNotFoundException(LoanNotFoundException ex) {
        log.error("Loan not found: {}", ex.getMessage(), ex);
        ApiResponseWrapper<String> response = new ApiResponseWrapper<>(
                HttpStatus.NOT_FOUND.value(),
                false,
                "Loan not found: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidLoanException.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleInvalidLoanException(InvalidLoanException ex) {
        log.error("Loan not found: {}", ex.getMessage(), ex);
        ApiResponseWrapper<String> response = new ApiResponseWrapper<>(
                HttpStatus.BAD_REQUEST.value(),
                false,
                "Invalid Loan: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LoanCreationException.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleLoanCreationException(LoanCreationException ex) {
        log.error("Loan creation failed: {}", ex.getMessage(), ex);
        ApiResponseWrapper<String> response = new ApiResponseWrapper<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                false,
                "Failed to create loan: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(LoanInterestRateNotFoundException.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleLoanInterestRateNotFoundException(LoanInterestRateNotFoundException ex) {
        log.error("Loan interest rate not found: {}", ex.getMessage(), ex);
        ApiResponseWrapper<String> response = new ApiResponseWrapper<>(
                HttpStatus.NOT_FOUND.value(),
                false,
                "Loan interest rate not found: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InterestRatePeriodOverlapException.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleInterestRatePeriodOverlapException(InterestRatePeriodOverlapException ex) {
        log.error("Interest rate period overlap: {}", ex.getMessage(), ex);
        ApiResponseWrapper<String> response = new ApiResponseWrapper<>(
                HttpStatus.BAD_REQUEST.value(),
                false,
                "Interest rate period overlap: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleFileStorageException(FileStorageException ex) {
        log.error("File storage error: {}", ex.getMessage(), ex);
        ApiResponseWrapper<String> response = new ApiResponseWrapper<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                false,
                "File storage error: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handler for InvalidRepaymentException
    @ExceptionHandler(InvalidRepaymentException.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleInvalidRepaymentException(InvalidRepaymentException ex) {
        log.error("Invalid repayment: {}", ex.getMessage(), ex);
        ApiResponseWrapper<String> response = new ApiResponseWrapper<>(
                HttpStatus.BAD_REQUEST.value(),
                false,
                "Invalid repayment: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handler for FundTransferException
    @ExceptionHandler(FundTransferException.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleFundTransferException(FundTransferException ex) {
        log.error("Fund transfer failed: {}", ex.getMessage(), ex);
        ApiResponseWrapper<String> response = new ApiResponseWrapper<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                false,
                "Fund transfer failed: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handler for AccountServiceException
    @ExceptionHandler(AccountServiceException.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleAccountServiceException(AccountServiceException ex) {
        log.error("Account service error: {}", ex.getMessage(), ex);
        ApiResponseWrapper<String> response = new ApiResponseWrapper<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                false,
                "Account service error: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handler for KafkaNotificationException
    @ExceptionHandler(KafkaNotificationException.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleKafkaNotificationException(KafkaNotificationException ex) {
        log.error("Kafka notification error: {}", ex.getMessage(), ex);
        ApiResponseWrapper<String> response = new ApiResponseWrapper<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                false,
                "Kafka notification error: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handler for RepaymentNotFoundException
    @ExceptionHandler(RepaymentNotFoundException.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleRepaymentNotFoundException(RepaymentNotFoundException ex) {
        log.error("Repayment not found: {}", ex.getMessage(), ex);
        ApiResponseWrapper<String> response = new ApiResponseWrapper<>(
                HttpStatus.NOT_FOUND.value(),
                false,
                "Repayment not found: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("Data integrity violation: {}", ex.getMessage(), ex);
        ApiResponseWrapper<String> response = new ApiResponseWrapper<>(
                HttpStatus.BAD_REQUEST.value(), // Sử dụng BAD_REQUEST thay vì INTERNAL_SERVER_ERROR
                false,
                "Data integrity violation occurred. Please check your data and try again.",
                null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Invalid input provided: {}", ex.getMessage(), ex);
        ApiResponseWrapper<String> response = new ApiResponseWrapper<>(
                HttpStatus.BAD_REQUEST.value(),
                false,
                "Invalid input: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleGeneralException(Exception ex) {
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
