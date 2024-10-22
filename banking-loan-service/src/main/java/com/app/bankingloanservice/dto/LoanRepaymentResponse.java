package com.app.bankingloanservice.dto;

import com.app.bankingloanservice.constant.PaymentStatus;
import lombok.*;

import java.time.LocalDate;

/**
 * Data Transfer Object for LoanRepayment entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LoanRepaymentResponse {

    private Long loanPaymentId;
    private Long loanId;
    private String loanContractNo;
    private String customerFullName;
    private String accountNumber;
    private Long principalAmount;
    private Long interestAmount;
    private Long latePaymentInterestAmount;
    private Long totalAmount;
    private LocalDate paymentDueDate;
    private LocalDate actualPaymentDate;
    private String transactionReference;
    private Boolean isLate;
    private PaymentStatus paymentStatus;

}