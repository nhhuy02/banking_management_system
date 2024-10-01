package com.app.bankingloanservice.dto;

import com.app.bankingloanservice.constant.CollateralStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO response for Collateral.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollateralResponse {

    @Schema(description = "Unique identifier for the collateral", example = "1")
    private Long collateralId;

    @Schema(description = "Identifier of the linked Loan", example = "1")
    private Long loanId;

    @Schema(description = "Identifier of the linked Loan Application", example = "1")
    private Long loanApplicationId;

    @Schema(description = "Document linked with the collateral")
    private DocumentResponse documentResponse;

    @Schema(description = "Type of collateral (e.g., house, car)", example = "House")
    private String collateralType;

    @Schema(description = "Value of the collateral in VND", example = "100000000")
    private Long collateralValue;

    @Schema(description = "Detailed description of the collateral", example = "A 3-bedroom house located in the city center.")
    private String description;

    @Schema(description = "Current status of the collateral", example = "ACTIVE")
    private CollateralStatus status;

    @Schema(description = "Date the collateral was reclaimed", example = "2024-01-15")
    private LocalDate reclaimDate;

    @Schema(description = "Reason for reclaiming the collateral", example = "Failure to repay debt on time")
    private String reasonForReclamation;

    @Schema(description = "Date the collateral was released", example = "2024-06-01")
    private LocalDate releaseDate;
}
