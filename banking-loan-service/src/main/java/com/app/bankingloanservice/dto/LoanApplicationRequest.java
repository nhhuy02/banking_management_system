package com.app.bankingloanservice.dto;

import com.app.bankingloanservice.constant.InterestRateType;
import com.app.bankingloanservice.constant.RepaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object for loan application")
public class LoanApplicationRequest {

    @Schema(description = "Account Number for receiving disbursement and making repayments", example = "98765")
    @NotNull(message = "{loanApplicationRequest.accountId.notNull}")
    private Long accountId;

    @NotNull(message = "{loanApplicationRequest.monthlyIncome.notNull}")
    @Min(value = 1000, message = "{loanApplicationRequest.monthlyIncome.min}")
    @Schema(description = "Customer's monthly income", example = "20000000")
    private Long monthlyIncome;

    @NotBlank(message = "{loanApplicationRequest.occupation.notBlank}")
    @Size(min = 2, max = 100, message = "{loanApplicationRequest.occupation.size}")
    @Schema(description = "Customer's occupation", example = "Software Engineer")
    private String occupation;

    @NotNull(message = "{loanApplicationRequest.loanTypeId.notNull}")
    @Min(value = 1, message = "{loanApplicationRequest.loanTypeId.min}")
    @Schema(description = "Loan type ID (reference to the LoanType entity)", example = "1")
    private Long loanTypeId;

    @NotNull(message = "{loanApplicationRequest.desiredLoanAmount.notNull}")
    @Min(value = 1000000, message = "{loanApplicationRequest.desiredLoanAmount.min}")
    @Schema(description = "Desired loan amount", example = "50000000")
    private Long desiredLoanAmount;

    @NotNull(message = "{loanApplicationRequest.desiredLoanTermMonths.notNull}")
    @Min(value = 1, message = "{loanApplicationRequest.desiredLoanTermMonths.min}")
    @Max(value = 480, message = "{loanApplicationRequest.desiredLoanTermMonths.max}")
    @Schema(description = "Desired loan term in months", example = "12")
    private Integer desiredLoanTermMonths;

    @NotNull(message = "{loanApplicationRequest.repaymentMethod.notNull}")
    @Schema(description = "Repayment method (e.g., EQUAL_INSTALLMENTS, REDUCING_BALANCE)", example = "EQUAL_INSTALLMENTS")
    private RepaymentMethod repaymentMethod;

    @NotNull(message = "{loanApplicationRequest.desiredDisbursementDate.notNull}")
    @FutureOrPresent(message = "{loanApplicationRequest.desiredDisbursementDate.futureOrPresent}")
    @Schema(description = "Desired disbursement date", example = "2024-10-01")
    private LocalDate desiredDisbursementDate;

    @NotNull(message = "{loanApplicationRequest.interestRateType.notNull}")
    @Schema(description = "Interest rate type (FIXED or FLOATING)", example = "FIXED")
    private InterestRateType interestRateType;

    @Size(max = 255, message = "{loanApplicationRequest.loanPurpose.size}")
    @Schema(description = "Customer's loan purpose", example = "Home Renovation")
    private String loanPurpose;

    @Schema(description = "Collateral details associated with the loan application")
    private CollateralRequest collateralRequest;
}
