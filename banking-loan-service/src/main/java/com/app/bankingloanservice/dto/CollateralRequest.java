package com.app.bankingloanservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Data Transfer Object for Collateral entity.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollateralRequest {

    @Schema(description = "Identifier of the associated loan application, can be null if used for registering a new loan application")
    private Long loanApplicationId;

    @Schema(description = "Document details related to the collateral")
    private DocumentUploadRequest documentUploadRequest;

    @Schema(description = "Type of collateral (e.g., Property, Vehicle, etc.)")
    @NotBlank(message = "{collateral.type.required}")
    private String collateralType;

    @Schema(description = "Value of the collateral in currency")
    @NotNull(message = "{collateral.value.required}")
    private Long collateralValue;

    @Schema(description = "Optional description of the collateral")
    private String description;
}
