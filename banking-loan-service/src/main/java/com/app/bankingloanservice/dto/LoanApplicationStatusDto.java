package com.app.bankingloanservice.dto;

import com.app.bankingloanservice.constant.ApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Loan application status update DTO")
public class LoanApplicationStatusDto {

    @NotNull(message = "Application status must not be null")
    @Schema(description = "Current status of the loan application", example = "APPROVED")
    private ApplicationStatus applicationStatus;

    @Schema(description = "Notes from the reviewer regarding the loan application", example = "Approved with conditions")
    private String reviewNotes;

}
