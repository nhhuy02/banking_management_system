package com.ojt.klb.controller;

import com.ojt.klb.dto.CardTypeDto;
import com.ojt.klb.response.ApiResponse;
import com.ojt.klb.service.CardTypeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/card-types")
@Validated
public class CardTypeController {

    private final static Logger logger = LoggerFactory.getLogger(CardTypeController.class);


    private final CardTypeService cardTypeService;

    public CardTypeController(CardTypeService cardTypeService) {
        this.cardTypeService = cardTypeService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> createCardType(@Valid @RequestBody CardTypeDto cardTypeDto) {
        logger.info("Received request to create new CardType: {}", cardTypeDto);
        Optional<CardTypeDto> savedCardType = cardTypeService.createCardType(cardTypeDto);
        if (savedCardType.isPresent()) {
            ApiResponse<String> response = new ApiResponse<>(
                    HttpStatus.CREATED.value(),
                    "CardType created successfully",
                    true,
                    null
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            ApiResponse<String> response = new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Failed to create CardType",
                    false,
                    null
            );
            logger.error("Failed to create CardType");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/IsActive")
    public ResponseEntity<ApiResponse<List<CardTypeDto>>> getAllCardTypesIsActive() {
        logger.info("Received request to fetch all CardTypes isActive");
        List<CardTypeDto> cardTypesIsActive = cardTypeService.getAllCardTypesIsActive();
        ApiResponse<List<CardTypeDto>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Fetched all active CardTypes",
                true,
                cardTypesIsActive
        );
        logger.info("Returning {} CardTypes Active", cardTypesIsActive.size());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CardTypeDto>>> getAllCardTypes() {
        logger.info("Received request to fetch all CardTypes");
        List<CardTypeDto> cardTypes = cardTypeService.getAllCardTypes();
        ApiResponse<List<CardTypeDto>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Fetched all CardTypes",
                true,
                cardTypes
        );
        logger.info("Returning {} CardTypes", cardTypes.size());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<String>> updateCardType(@PathVariable Long id,
                                                              @Valid @RequestBody CardTypeDto cardTypeDto) {
        cardTypeService.updateCardType(id, cardTypeDto);
        ApiResponse<String> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "CardType updated successfully",
                true,
                null
        );
        logger.info("Update card type successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
