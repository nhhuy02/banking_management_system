package com.app.bankingloanservice.repository;

import com.app.bankingloanservice.entity.LoanRepayment;
import com.app.bankingloanservice.constant.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepaymentRepository extends JpaRepository<LoanRepayment, Long> {

    Page<LoanRepayment> findByLoanLoanId(Long loanId, Pageable pageable);

    @Query("SELECT lr FROM LoanRepayment lr WHERE lr.paymentDueDate = :dueDate AND lr.paymentStatus = :status")
    List<LoanRepayment> findByPaymentDueDate(LocalDate dueDate, PaymentStatus status);

    List<LoanRepayment> findByPaymentDueDateBeforeAndPaymentStatus(LocalDate date, PaymentStatus status);
}