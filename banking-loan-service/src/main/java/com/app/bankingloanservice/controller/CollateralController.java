package com.app.bankingloanservice.controller;

import com.app.bankingloanservice.dto.ApiResponseWrapper;
import com.app.bankingloanservice.dto.DocumentResponseDto;
import com.app.bankingloanservice.dto.DocumentUploadDto;
import com.app.bankingloanservice.service.CollateralService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/collaterals")
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
    @PostMapping("/{collateralId}/documents")
    public ResponseEntity<ApiResponseWrapper<DocumentResponseDto>> uploadCollateralDocument(
            @PathVariable("collateralId") Long collateralId,
            @ModelAttribute DocumentUploadDto documentUploadDto) throws Exception {

        log.info("Uploading document for collateral with ID: {}", collateralId);

        // Call the collateral service to create and link the document to the collateral
        DocumentResponseDto documentResponse = collateralService.uploadCollateralDocument(collateralId, documentUploadDto);

        // Create the API response wrapper
        ApiResponseWrapper<DocumentResponseDto> response = new ApiResponseWrapper<>(
                HttpStatus.CREATED.value(),
                true,
                "Document uploaded successfully!",
                documentResponse
        );

        // Return the response with HTTP status 201 CREATED
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
