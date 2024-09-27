package com.ojt.klb.controller;

import com.ojt.klb.dto.CardRegistrationRequestDto;
import com.ojt.klb.dto.CardRegistrationRequestResponseDto;
import com.ojt.klb.dto.CardRegistrationRequestUpdateDto;
import com.ojt.klb.exception.CardNotFoundException;
import com.ojt.klb.model.CardRegistrationRequest;
import com.ojt.klb.response.ApiResponse;
import com.ojt.klb.service.CardRegistrationRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/card-registration")
public class CardRegistrationRequestController {

    private static final Logger logger = LoggerFactory.getLogger(CardRegistrationRequestController.class);
    private final CardRegistrationRequestService cardRegistrationRequestService;

    public CardRegistrationRequestController(CardRegistrationRequestService cardRegistrationRequestService) {
        this.cardRegistrationRequestService = cardRegistrationRequestService;
    }

    @PostMapping("/register/{accountId}")
    public ResponseEntity<ApiResponse<CardRegistrationRequestDto>> registerCard(
            @PathVariable Long accountId,
            @RequestBody CardRegistrationRequestDto cardRegistrationRequestDto) {

        try {
            var result = cardRegistrationRequestService.registerCard(accountId, cardRegistrationRequestDto);
            if (result.isPresent()) {
                logger.info("Card registration request created successfully for accountId: {}", accountId);
                ApiResponse<CardRegistrationRequestDto> response = new ApiResponse<>(
                        HttpStatus.CREATED.value(),
                        "Card registration request created successfully.",
                        true,
                        result.get()
                );
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                logger.error("Failed to create card registration request for accountId: {}", accountId);
                ApiResponse<CardRegistrationRequestDto> response = new ApiResponse<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Failed to create card registration request. Please check the provided details.",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            logger.error("Error while registering card for accountId {}: {}", accountId, e.getMessage());
            ApiResponse<CardRegistrationRequestDto> response = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An error occurred while processing your request.",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/pending-requests")
    public ResponseEntity<ApiResponse<List<CardRegistrationRequestResponseDto>>> getAllPendingCardRequests() {
        Optional<List<CardRegistrationRequestResponseDto>> optionalCardRequests = cardRegistrationRequestService.getAllCardRegistrationRequestsStatusPending();

        if (optionalCardRequests.isPresent()) {
            ApiResponse<List<CardRegistrationRequestResponseDto>> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Pending card registration requests retrieved successfully",
                    true,
                    optionalCardRequests.get()
            );
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            ApiResponse<List<CardRegistrationRequestResponseDto>> response = new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    "No pending card registration requests found",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/{accountId}/status")
    public ResponseEntity<ApiResponse<String>> updateCardRegistrationRequestStatusPending(
            @PathVariable Long accountId,
            @RequestBody CardRegistrationRequestUpdateDto dto) {

        try {
            cardRegistrationRequestService.updateCardRegistrationRequestStatusPending(accountId, dto);

            ApiResponse<String> response = new ApiResponse<>(
                    HttpStatus.CREATED.value(),
                    "Update data success",
                    true,
                    null
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (CardNotFoundException e) {
            logger.error("Card not found for accountId: {}", accountId, e);
            throw e;

        } catch (DataAccessException e) {
            logger.error("Database error while updating card registration for accountId: {}", accountId, e);
            throw e;

        } catch (Exception e) {
            logger.error("Unexpected error while updating card registration for accountId: {}", accountId, e);
            throw e;
        }
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<ApiResponse<List<CardRegistrationRequestResponseDto>>> getAllCardRegistrationRequestsByAccountId(
            @PathVariable Long accountId) {
        try {
            Optional<List<CardRegistrationRequestResponseDto>> optionalDtoList = cardRegistrationRequestService.getAllCardRegistrationRequestsByAccountId(accountId);

            if (optionalDtoList.isPresent() && !optionalDtoList.get().isEmpty()) {
                ApiResponse<List<CardRegistrationRequestResponseDto>> response = new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Fetch successful",
                        true,
                        optionalDtoList.get()
                );
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<List<CardRegistrationRequestResponseDto>> response = new ApiResponse<>(
                        HttpStatus.NOT_FOUND.value(),
                        "No requests found for this accountId",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            logger.error("Error fetching card registration requests for accountId: {}", accountId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
