package com.app.bankingloanservice.dto;

import com.app.bankingloanservice.constant.SettlementStatus;
import lombok.*;

import java.time.LocalDate;

/**
 * Data Transfer Object cho thực thể LoanSettlement.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanSettlementResponse {

    private Long loanSettlementId;
    private Long loanId;
    private LocalDate settlementDate;
    private Long pastDueInterestAmount;
    private Long latePaymentInterestAmount;
    private Long prepaymentPenaltyAmount;
    private Long settlementAmount;
    private SettlementStatus settlementStatus;

}