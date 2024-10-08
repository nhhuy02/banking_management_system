package com.app.bankingloanservice.repository;

import com.app.bankingloanservice.constant.LoanStatus;
import com.app.bankingloanservice.entity.LoanRepayment;
import com.app.bankingloanservice.constant.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepaymentRepository extends JpaRepository<LoanRepayment, Long> {

    /**
     * Find all loan repayments by loan ID and order by paymentDueDate.
     *
     * @param loanId the ID of the loan
     * @return a list of loan repayments ordered by paymentDueDate
     */
    List<LoanRepayment> findByLoanLoanIdOrderByPaymentDueDateAsc(Long loanId);

    Page<LoanRepayment> findByLoanLoanId(Long loanId, Pageable pageable);

    @Query("SELECT lr FROM LoanRepayment lr WHERE lr.paymentDueDate = :dueDate AND lr.paymentStatus = :status")
    List<LoanRepayment> findByPaymentDueDate(LocalDate dueDate, PaymentStatus status);

    List<LoanRepayment> findByPaymentDueDateBeforeAndPaymentStatus(LocalDate date, PaymentStatus status);

    @Query("SELECT lr FROM LoanRepayment lr " +
            "JOIN lr.loan l " +
            "WHERE lr.accountId = :accountId " +
            "AND l.status = :loanStatus")
    List<LoanRepayment> findByAccountIdAndLoanStatus(
            @Param("accountId") Long accountId,
            @Param("loanStatus") LoanStatus loanStatus
    );

    /**
     * Finds loan repayments that are eligible for payment within the payment window for a given account.
     *
     * @param accountId         The ID of the account for which to find repayments.
     * @param loanStatus        The status of the loan (e.g., ACTIVE) that the repayments are associated with.
     * @param paymentStatus     The payment status to exclude (e.g., PAID).
     * @param paymentCutOffDate The cut-off date for repayments that are still due (must be before this date).
     * @return A list of LoanRepayment objects that meet the specified criteria.
     */
    @Query("SELECT lr FROM LoanRepayment lr WHERE lr.accountId = :accountId " +
            "AND lr.loan.status = :loanStatus " +
            "AND lr.paymentStatus != :paymentStatus " +
            "AND lr.paymentDueDate < :paymentCutOffDate")
    //    (paymentDueDate - 30) < now
    // <=> paymentDueDate < (now + 30)
    // <=> paymentDueDate < paymentCutOffDate
    List<LoanRepayment> findByAccountIdAndWithinPaymentWindowAndUnpaid(
            @Param("accountId") Long accountId,
            @Param("loanStatus") LoanStatus loanStatus,
            @Param("paymentStatus") PaymentStatus paymentStatus,
            @Param("paymentCutOffDate") LocalDate paymentCutOffDate
    );

}