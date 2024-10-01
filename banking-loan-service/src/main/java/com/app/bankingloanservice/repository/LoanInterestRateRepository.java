package com.app.bankingloanservice.repository;

import com.app.bankingloanservice.entity.LoanInterestRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanInterestRateRepository extends JpaRepository<LoanInterestRate, Long> {

    // Retrieve the interest rate history for a specific loan
    List<LoanInterestRate> findByLoanLoanId(Long loanId);

    // Retrieve the current interest rate for a loan based on a specific date
    @Query("SELECT l FROM LoanInterestRate l WHERE l.loan.loanId = :loanId AND :date BETWEEN l.effectiveFrom AND l.effectiveTo")
    LoanInterestRate findCurrentInterestRateByLoanIdAndDate(Long loanId, LocalDate date);

    // Retrieve all interest rates within a specified date range for a specific loan
    @Query("SELECT l FROM LoanInterestRate l WHERE l.loan.loanId = :loanId AND l.effectiveFrom <= :endDate AND (l.effectiveTo IS NULL OR l.effectiveTo >= :startDate)")
    List<LoanInterestRate> findInterestRatesInDateRange(Long loanId, LocalDate startDate, LocalDate endDate);
}
