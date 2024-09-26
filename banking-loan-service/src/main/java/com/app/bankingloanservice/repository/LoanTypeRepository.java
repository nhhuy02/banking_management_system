package com.app.bankingloanservice.repository;

import com.app.bankingloanservice.entity.LoanType;
import com.app.bankingloanservice.entity.Loan;
import com.app.bankingloanservice.entity.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanTypeRepository extends JpaRepository<LoanType, Long> {

    // Find a loan type by its name
    Optional<LoanType> findByLoanTypeName(String loanTypeName);

    // Find LoanType By ID:
    Optional<LoanType> findByLoanTypeId(Long loanTypeId);

    // Find all active loan types
    List<LoanType> findByIsActiveTrue();

    // Find loan types that require collateral
    List<LoanType> findByRequiresCollateralTrue();

    // Find loan types by maximum loan amount
    List<LoanType> findByMaxLoanAmountGreaterThanEqual(Long maxLoanAmount);

    // Find loan types by maximum loan term in months
    List<LoanType> findByMaxLoanTermMonthsLessThanEqual(Integer maxLoanTermMonths);

    // Custom query to find loan types by a range of annual interest rates
    @Query("SELECT lt FROM LoanType lt WHERE lt.annualInterestRate BETWEEN :minRate AND :maxRate")
    List<LoanType> findByAnnualInterestRateBetween(@Param("minRate") BigDecimal minRate, @Param("maxRate") BigDecimal maxRate);

    // Custom query to find loan types by created date range
    @Query("SELECT lt FROM LoanType lt WHERE lt.createdDate BETWEEN :startDate AND :endDate")
    List<LoanType> findByCreatedDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Custom query to find all loans associated with a specific loan type
    @Query("SELECT l FROM Loan l WHERE l.loanType.loanTypeId = :loanTypeId")
    List<Loan> findLoansByLoanTypeId(@Param("loanTypeId") Long loanTypeId);

    // Custom query to find all loan applications associated with a specific loan type
    @Query("SELECT la FROM LoanApplication la WHERE la.loanType.loanTypeId = :loanTypeId")
    List<LoanApplication> findLoanApplicationsByLoanTypeId(@Param("loanTypeId") Long loanTypeId);
}
