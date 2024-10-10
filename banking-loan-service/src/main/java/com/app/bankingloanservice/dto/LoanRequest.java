package com.app.bankingloanservice.dto;

import com.app.bankingloanservice.constant.InterestRateType;
import com.app.bankingloanservice.constant.RepaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO for creating a loan.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanRequest {

    @Schema(description = "ID of the loan application associated with this loan", example = "2")
    @NotNull(message = "{loanRequest.loanApplicationId.notNull}")
    private Long loanApplicationId;

    @Schema(description = "ID of the account who is taking the loan", example = "456")
    @NotNull(message = "{loanRequest.accountId.notNull}")
    private Long accountId;

    @Schema(description = "Loan type ID", example = "3")
    @NotNull(message = "{loanRequest.loanTypeId.notNull}")
    private Long loanTypeId;

    @Schema(description = "Amount of the loan in VND", example = "100000000")
    @NotNull(message = "{loanRequest.loanAmount.notNull}")
    @Min(value = 1000000, message = "{loanRequest.loanAmount.min}")
    private Long loanAmount;

    @Schema(description = "Type of interest rate, either FIXED or FLOATING", example = "FIXED")
    @NotNull(message = "{loanRequest.interestRateType.notNull}")
    private InterestRateType interestRateType;

    @Schema(description = "Interest rate details for the loan")
    @NotNull(message = "{loanRequest.currentInterestRate.notNull}")
    private LoanInterestRateRequest currentInterestRate;

    @Schema(description = "Repayment method for the loan", example = "EQUAL_INSTALLMENTS")
    @NotNull(message = "{loanRequest.repaymentMethod.notNull}")
    private RepaymentMethod repaymentMethod;

    @Schema(description = "Term of the loan in months", example = "60")
    @NotNull(message = "{loanRequest.loanTermMonths.notNull}")
    @Min(value = 6, message = "{loanRequest.loanTermMonths.min}")
    @Max(value = 360, message = "{loanRequest.loanTermMonths.max}")
    private Integer loanTermMonths;

    @Schema(description = "Disbursement date of the loan", example = "2023-10-01")
    @NotNull(message = "{loanRequest.disbursementDate.notNull}")
    @FutureOrPresent(message = "{loanRequest.disbursementDate.futureOrPresent}")
    private LocalDate disbursementDate;
}
