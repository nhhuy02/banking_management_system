package com.app.bankingloanservice.dto;

import com.app.bankingloanservice.constant.ApplicationStatus;
import com.app.bankingloanservice.constant.RepaymentMethod;
import com.app.bankingloanservice.constant.InterestRateType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationResponse {

    private Long loanApplicationId; // Loan application ID

    private Long customerId; // Customer ID

    private String customerName;

    private Long accountId; // Account ID for receiving disbursement and making repayments

    private String contactPhone; // Customer's phone number

    private String contactEmail; // Customer's email

    private String idCard; // Customer's ID Card number

    private Long monthlyIncome; // Monthly income

    private String occupation; // Occupation

    private LoanTypeDto loanTypeDto;

    private Long desiredLoanAmount; // Desired loan amount

    private Integer desiredLoanTermMonths; // Loan term in months

    private RepaymentMethod repaymentMethod; // Repayment method

    private LocalDate desiredDisbursementDate; // Desired disbursement date

    private InterestRateType interestRateType; // Interest rate type

    private String loanPurpose; // Loan purpose

    private Integer creditScore; // Credit score

    private ApplicationStatus applicationStatus; // Application status

    private LocalDate submissionDate; // Submission date

    private LocalDate reviewDueDate; // Review due date

    private LocalDate reviewDate; // Review date

    private String reviewNotes; // Review notes

    private CollateralResponse collateralResponse;

    private DocumentResponse documentResponse;
}
