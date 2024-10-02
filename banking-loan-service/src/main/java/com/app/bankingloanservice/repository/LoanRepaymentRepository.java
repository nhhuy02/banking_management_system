package com.app.bankingloanservice.repository;

import com.app.bankingloanservice.entity.LoanRepayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepaymentRepository extends JpaRepository<LoanRepayment, Long> {
    Page<LoanRepayment> findByLoanLoanId(Long loanId, Pageable pageable);
}