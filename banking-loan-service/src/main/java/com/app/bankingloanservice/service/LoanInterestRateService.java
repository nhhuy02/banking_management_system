package com.app.bankingloanservice.service;

import com.app.bankingloanservice.dto.LoanInterestRateRequest;
import com.app.bankingloanservice.entity.LoanInterestRate;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface LoanInterestRateService {

    // Create an interest rate
    @Transactional
    LoanInterestRate createLoanInterestRate(Long loanId, LoanInterestRateRequest loanInterestRateRequest);

    LoanInterestRate updateEffectiveToDate(Long loanInterestRateId, LocalDate effectiveTo);

    // Find interest rate history by loanId
    List<LoanInterestRate> getInterestRateHistory(Long loanId);

    // Get the current interest rate of a loan by date
    LoanInterestRate getCurrentInterestRate(Long loanId, LocalDate date);

    // Get interest rates in a specified date range
    List<LoanInterestRate> getInterestRatesInDateRange(Long loanId, LocalDate startDate, LocalDate endDate);

    // Delete an interest rate record by id
    void deleteLoanInterestRate(Long loanInterestRateId);
}
