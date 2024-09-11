package com.ctv_it.customer_service.controller;

import com.ctv_it.customer_service.dto.KycDto;
import com.ctv_it.customer_service.dto.KycResponseDto;
import com.ctv_it.customer_service.service.KycService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
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

    @Autowired
    private KycService kycService;

    @PutMapping("/{id}")
    public ResponseEntity<String> updateKyc(@PathVariable Long id, @Valid @RequestBody KycDto updatedKyc) {
        try {
            KycDto kyc = kycService.updateKyc(id, updatedKyc);
            String successMessage = "KYC with ID " + id + " updated successfully.";
            logger.info(successMessage);
            return ResponseEntity.ok(successMessage);
        } catch (EntityNotFoundException ex) {
            String errorMessage = "KYC with ID " + id + " not found.";
            logger.warn(errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        } catch (MethodArgumentNotValidException ex) {
            String errorMessage = "Validation failed: " + ex.getBindingResult().getAllErrors().toString();
            logger.warn(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        } catch (Exception ex) {
            String errorMessage = "An error occurred while updating KYC.";
            logger.error("Error updating KYC with ID " + id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getKycById(@PathVariable Long id) {
        logger.info("Fetching KYC details for id: {}", id);

        Optional<KycResponseDto> kycResponseDtoOptional = kycService.getKycById(id);

        if (kycResponseDtoOptional.isPresent()) {
            KycResponseDto kycResponseDto = kycResponseDtoOptional.get();

            if (kycResponseDto.getVerificationStatus().equals("pending") &&
                    (kycResponseDto.getDocumentType() == null && kycResponseDto.getDocumentNumber() == null)) {
                logger.warn("KYC details for id: {} are incomplete, requires update.", id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("KYC details are incomplete. Please update your KYC information.");
            } else {
                logger.info("Successfully fetched KYC details for id: {}", id);
                return ResponseEntity.ok(kycResponseDto);
            }
        } else {
            logger.warn("KYC not found for id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("KYC not found with id: " + id);
        }
    }
}

