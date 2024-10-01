package com.app.bankingloanservice.controller;

import com.app.bankingloanservice.dto.ApiResponseWrapper;
import com.app.bankingloanservice.dto.DocumentResponse;
import com.app.bankingloanservice.dto.DocumentUploadRequest;
import com.app.bankingloanservice.service.CollateralService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/collaterals")
@RequiredArgsConstructor
@Tag(name = "Collateral", description = "APIs for managing collaterals")
@Slf4j
public class CollateralController {

    private final CollateralService collateralService;

    @Operation(
            summary = "Upload a document to collateral",
            description = "Uploads a document and links it to the specified collateral using the collateralId.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Document uploaded successfully", content = @Content(schema = @Schema(implementation = ApiResponseWrapper.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid collateralId or document data", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Collateral not found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @PostMapping(value = "/{collateralId}/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseWrapper<DocumentResponse>> uploadCollateralDocument(
            @PathVariable("collateralId") Long collateralId,
            @ModelAttribute DocumentUploadRequest documentUploadRequest) {

        log.info("Uploading document for collateral with ID: {}", collateralId);

        // Call the collateral service to create and link the document to the collateral
        DocumentResponse documentResponse = collateralService.uploadCollateralDocument(collateralId, documentUploadRequest);

        // Create the API response wrapper
        ApiResponseWrapper<DocumentResponse> response = new ApiResponseWrapper<>(
                HttpStatus.CREATED.value(),
                true,
                "Document uploaded successfully!",
                documentResponse
        );

        // Return the response with HTTP status 201 CREATED
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
