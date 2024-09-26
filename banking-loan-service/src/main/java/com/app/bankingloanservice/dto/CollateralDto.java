package com.app.bankingloanservice.dto;

import com.app.bankingloanservice.constant.CollateralStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

/**
 * Data Transfer Object for Collateral entity.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollateralDto {

    @Schema(description = "Unique identifier for the collateral")
    private Long collateralId;

    @Schema(description = "Identifier of the associated loan, required if the collateral is linked to an existing loan")
    private Long loanId;

    @Schema(description = "Identifier of the associated loan application, can be null if used for registering a new loan application")
    private Long loanApplicationId;

    @Schema(description = "Document details related to the collateral")
    private DocumentUploadDto documentUploadDto;

    @Schema(description = "Type of collateral (e.g., Property, Vehicle, etc.)")
    private String collateralType;

    @Schema(description = "Value of the collateral in currency")
    private Long collateralValue;

    @Schema(description = "Optional description of the collateral")
    private String description;

    @Schema(description = "Current status of the collateral")
    private CollateralStatus status;

    @Schema(description = "Date when the collateral can be reclaimed")
    private LocalDate reclaimDate;

    @Schema(description = "Reason for reclaiming the collateral")
    private String reasonForReclamation;

    @Schema(description = "Date when the collateral was released")
    private LocalDate releaseDate;
}
