package com.ctv_it.customer_service.controller;

import com.ctv_it.customer_service.dto.KycDto;
import com.ctv_it.customer_service.dto.KycResponseDto;
import com.ctv_it.customer_service.response.ApiResponse;
import com.ctv_it.customer_service.service.KycService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/customer/kyc")
@Validated
public class KycController {

    private static final Logger logger = LoggerFactory.getLogger(KycController.class);

    private final KycService kycService;
    public KycController(KycService kycService) {
        this.kycService = kycService;
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<ApiResponse<String>> updateKyc(@PathVariable Long customerId, @Valid @RequestBody KycDto updatedKyc) {
        try {
            KycDto kyc = kycService.updateKycbyAccountId(customerId, updatedKyc);
            String successMessage = "KYC with customerId " + customerId + " updated successfully.";
            logger.info(successMessage);
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), successMessage, true, null));
        } catch (EntityNotFoundException ex) {
            String errorMessage = "KYC with customerId " + customerId + " not found.";
            logger.warn(errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), errorMessage, false, null));
        } catch (IllegalArgumentException ex) {
            String errorMessage = ex.getMessage();
            logger.warn(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), errorMessage, false, null));
        } catch (Exception ex) {
            String errorMessage = "An error occurred while updating KYC.";
            logger.error("Error updating KYC with customerId {}", customerId, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage, false, null));
        }
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<KycResponseDto>> getKycByCustomerId(@PathVariable Long customerId) {
        logger.info("Fetching KYC details for id: {}", customerId);
        Optional<KycResponseDto> kycResponseDtoOptional = kycService.getKycByCustomerId(customerId);

        if (kycResponseDtoOptional.isPresent()) {
            KycResponseDto kycResponseDto = kycResponseDtoOptional.get();

            if ("pending".equals(kycResponseDto.getVerificationStatus()) &&
                    kycResponseDto.getDocumentType() == null && kycResponseDto.getDocumentNumber() == null) {
                logger.warn("KYC details for id: {} are incomplete, requires update.", customerId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "KYC details are incomplete. Please update your KYC information.", false, null));
            } else {
                logger.info("Successfully fetched KYC details for CustomerId: {}", customerId);
                return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "KYC details retrieved successfully", true, kycResponseDto));
            }
        } else {
            logger.warn("KYC not found for customerId: {}", customerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "KYC not found with id: " + customerId, false, null));
        }
    }
}
