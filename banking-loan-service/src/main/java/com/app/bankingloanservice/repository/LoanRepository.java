package com.app.bankingloanservice.repository;

import com.app.bankingloanservice.constant.RepaymentMethod;
import com.app.bankingloanservice.entity.Loan;
import com.app.bankingloanservice.constant.LoanStatus;
import com.app.bankingloanservice.constant.CustomerConfirmationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    // Find loan by contract number
    Optional<Loan> findByLoanContractNo(String loanContractNo);

    /**
     * Find loans based on customerId with pagination support.
     *
     * @param customerId The customer's ID.
     * @param pageable   Paging information.
     * @return Loans page found.
     */
    Page<Loan> findByCustomerId(Long customerId, Pageable pageable);

    boolean existsByLoanContractNo(String loanContractNo);

    // Find loans by status
    List<Loan> findByStatus(LoanStatus status);

    // Find loans by customer confirmation status
    List<Loan> findByCustomerConfirmationStatus(CustomerConfirmationStatus status);

    // Find loans with disbursement date in a specific range
    List<Loan> findByDisbursementDateBetween(LocalDate startDate, LocalDate endDate);

    // Find overdue loans (maturity date < current date and status is ACTIVE)
    @Query("SELECT l FROM Loan l WHERE l.maturityDate < CURRENT_DATE AND l.status = 'ACTIVE'")
    List<Loan> findOverdueLoans();

    // Find bad debts
    List<Loan> findByIsBadDebtTrue();

    // Calculate total remaining balance of all loans
    @Query("SELECT SUM(l.remainingBalance) FROM Loan l")
    Long calculateTotalRemainingBalance();

    // Find loans by loan type and status
    @Query("SELECT l FROM Loan l WHERE l.loanType.loanTypeId = :loanTypeId AND l.status = :status")
    List<Loan> findByLoanTypeAndStatus(@Param("loanTypeId") Long loanTypeId, @Param("status") LoanStatus status);

    // Find loans with amount greater than a specific value
    List<Loan> findByLoanAmountGreaterThan(Long amount);

    // Count the number of loans by status
    Long countByStatus(LoanStatus status);

    // Find loans with maturity date in the current month
    @Query("SELECT l FROM Loan l WHERE YEAR(l.maturityDate) = YEAR(CURRENT_DATE) AND MONTH(l.maturityDate) = MONTH(CURRENT_DATE)")
    List<Loan> findLoansMaturingThisMonth();

    // Find loans by repayment method
    List<Loan> findByRepaymentMethod(RepaymentMethod repaymentMethod);

    // Find loans with renewal count greater than a specific value
    List<Loan> findByRenewalCountGreaterThan(Integer count);
}
