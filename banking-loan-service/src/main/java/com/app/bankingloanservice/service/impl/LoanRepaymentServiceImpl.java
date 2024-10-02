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
        // 1. Kiểm tra xem Loan có tồn tại không
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan with ID " + loanId + " not found"));

        // 2. Kiểm tra trạng thái khoản vay
        if (loan.getStatus() != LoanStatus.ACTIVE) {
            throw new InvalidLoanException("Loan is not active for repayment.");
        }

        // 3. Kiểm tra lịch trả nợ có tồn tại không
        LoanRepayment repayment = loanRepaymentRepository.findById(repaymentId)
                .orElseThrow(() -> new RepaymentNotFoundException("Repayment with ID " + repaymentId + " not found"));

        // 4. Kiểm tra xem repayment thuộc về loan này không
        if (!repayment.getLoan().getLoanId().equals(loanId)) {
            throw new InvalidRepaymentException("Repayment does not belong to the specified loan.");
        }

        // 5. Kiểm tra trạng thái của repayment
        if (repayment.getPaymentStatus() == PaymentStatus.PAID) {
            throw new InvalidRepaymentException("This repayment has already been paid.");
        }

        // 6. Xác định xem có phải thanh toán trễ không
        boolean isLate = repayment.getPaymentDueDate().isBefore(LocalDate.now());
        BigDecimal penalty = BigDecimal.ZERO;
        if (isLate) {
            // Tính lãi phạt
            BigDecimal penaltyRate = loan.getCurrentInterestRate().getPrepaymentPenaltyRate();
            penalty = loan.getRemainingBalance().multiply(penaltyRate)
                    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);

            repayment.setLatePaymentInterestAmount(penalty);
            repayment.setIsLate(true);
        }

        // 7. Cập nhật repayment
        repayment.setActualPaymentDate(LocalDate.now());
        repayment.setPaymentStatus(PaymentStatus.PAID);
        repayment.setIsLate(isLate);
        loanRepaymentRepository.save(repayment);

        // 8. Cập nhật thông tin khoản vay
        BigDecimal paymentAmount = repaymentRequest.getAmount();
        if (isLate) {
            paymentAmount = paymentAmount.subtract(penalty);
        }
        loan.setTotalPaidAmount(loan.getTotalPaidAmount().add(paymentAmount));
        loan.setRemainingBalance(loan.getLoanAmount().subtract(loan.getTotalPaidAmount()));
        loanRepository.save(loan);

        // 9. Kiểm tra xem khoản vay đã được thanh toán đầy đủ chưa
        if (loan.getRemainingBalance().compareTo(BigDecimal.ZERO) <= 0) {
            loan.setStatus(LoanStatus.SETTLED);
            loan.setSettlementDate(LocalDate.now());
            loanRepository.save(loan);
        }
    }

    @Override
    public Page<LoanRepaymentResponse> getRepaymentSchedule(Long loanId, Pageable pageable) {
        Page<LoanRepayment> repayments = loanRepaymentRepository.findByLoanLoanId(loanId, pageable);
        return repayments.map(loanRepaymentMapper::toResponse);
    }
}