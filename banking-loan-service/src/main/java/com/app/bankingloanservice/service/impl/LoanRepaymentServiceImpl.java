package com.app.bankingloanservice.service.impl;

import com.app.bankingloanservice.dto.LoanRepaymentResponse;
import com.app.bankingloanservice.mapper.LoanRepaymentMapper;
import com.app.bankingloanservice.util.RepaymentCalculator;
import com.app.bankingloanservice.util.RepaymentCalculatorFactory;
import com.app.bankingloanservice.dto.RepaymentRequest;
import com.app.bankingloanservice.entity.Loan;
import com.app.bankingloanservice.entity.LoanRepayment;
import com.app.bankingloanservice.constant.LoanStatus;
import com.app.bankingloanservice.constant.PaymentStatus;
import com.app.bankingloanservice.exception.InvalidLoanException;
import com.app.bankingloanservice.exception.InvalidRepaymentException;
import com.app.bankingloanservice.exception.LoanNotFoundException;
import com.app.bankingloanservice.exception.RepaymentNotFoundException;
import com.app.bankingloanservice.repository.LoanRepaymentRepository;
import com.app.bankingloanservice.repository.LoanRepository;
import com.app.bankingloanservice.service.LoanRepaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class LoanRepaymentServiceImpl implements LoanRepaymentService {

    private final LoanRepaymentRepository loanRepaymentRepository;
    private final LoanRepository loanRepository;
    private final RepaymentCalculatorFactory repaymentCalculatorFactory;
    private final LoanRepaymentMapper loanRepaymentMapper;

    @Override
    public void createRepaymentSchedule(Loan loan) {
        RepaymentCalculator calculator = repaymentCalculatorFactory.getRepaymentCalculator(loan.getRepaymentMethod());
        calculator.calculateRepaymentSchedule(loan);
    }

    @Override
    public void makeRepayment(Long loanId, Long repaymentId, RepaymentRequest repaymentRequest) {
        // 1. Check if the loan exists
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan with ID " + loanId + " not found"));

        // 2. Check the status of the loan
        if (loan.getStatus() != LoanStatus.ACTIVE) {
            throw new InvalidLoanException("Loan is not active for repayment.");
        }

        // 3. Check if the repayment schedule exists
        LoanRepayment repayment = loanRepaymentRepository.findById(repaymentId)
                .orElseThrow(() -> new RepaymentNotFoundException("Repayment with ID " + repaymentId + " not found"));

        // 4. Check if the repayment belongs to this loan
        if (!repayment.getLoan().getLoanId().equals(loanId)) {
            throw new InvalidRepaymentException("Repayment does not belong to the specified loan.");
        }

        // 5. Check the status of the repayment
        if (repayment.getPaymentStatus() == PaymentStatus.PAID) {
            throw new InvalidRepaymentException("This repayment has already been paid.");
        }

        // 6. Determine if it is a late payment
        boolean isLate = repayment.getPaymentDueDate().isBefore(LocalDate.now());
        BigDecimal latePaymentInterest = BigDecimal.ZERO;
        if (isLate) {
            // Calculate late payment interest
            BigDecimal latePaymentInterestRate = loan.getCurrentInterestRate().getLatePaymentInterestRate();
            latePaymentInterest = repayment.getInterestAmount().multiply(latePaymentInterestRate)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            repayment.setLatePaymentInterestAmount(latePaymentInterest);
            repayment.setIsLate(true);
        }

        // 7. Update repayment details
        repayment.setActualPaymentDate(LocalDate.now());
        repayment.setPaymentStatus(PaymentStatus.PAID);
        repayment.setIsLate(isLate);
        loanRepaymentRepository.save(repayment);

        // 8. Update loan information
        BigDecimal paymentAmount = repaymentRequest.getAmount();
        // Add the amount of the current payment to the total loan amount paid:
        loan.setTotalPaidAmount(loan.getTotalPaidAmount().add(paymentAmount));
        // Update the remaining balance by subtracting the principal balance paid in this payment:
        loan.setRemainingBalance(loan.getRemainingBalance().subtract(repayment.getPrincipalAmount()));
        loanRepository.save(loan);

        // 9. Check if the loan has been fully repaid
        if (loan.getRemainingBalance().compareTo(BigDecimal.ZERO) <= 0) {
            loan.setSettlementDate(LocalDate.now());
            if (isLate) {
                loan.setStatus(LoanStatus.SETTLED_LATE);
            } else if (repayment.getPaymentDueDate().isAfter(LocalDate.now())) {
                loan.setStatus(LoanStatus.SETTLED_EARLY);  // If paid before due date
            } else {
                loan.setStatus(LoanStatus.SETTLED_ON_TIME);
            }
            loanRepository.save(loan);
        }
    }

    @Override
    public Page<LoanRepaymentResponse> getRepaymentSchedule(Long loanId, Pageable pageable) {
        Page<LoanRepayment> repayments = loanRepaymentRepository.findByLoanLoanId(loanId, pageable);
        return repayments.map(loanRepaymentMapper::toResponse);
    }
}
