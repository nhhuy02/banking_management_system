package com.ojt.klb.controller;

import com.ojt.klb.response.ApiResponse;
import com.ojt.klb.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private static final Logger logger = LoggerFactory.getLogger(CardController.class);

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/registerNapasCard")
    public ResponseEntity<ApiResponse<String>> registerNapasCard(@RequestParam Long accountId) {
        try {
            cardService.registerCardNapas(accountId);
            logger.info("Card Napas created successfully for accountId: {}", accountId);

            ApiResponse<String> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Register Napas card successfully",
                    true,
                    null
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error creating card Napas for accountId: {}", accountId, e);

            ApiResponse<String> response = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error creating Napas card. Try again later!",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
