package com.app.bankingloanservice.service;

import com.app.bankingloanservice.constant.LoanStatus;
import com.app.bankingloanservice.dto.LoanRequest;
import com.app.bankingloanservice.dto.LoanResponse;
import com.app.bankingloanservice.entity.Loan;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.springframework.transaction.annotation.Transactional;

public interface LoanService {

  LoanResponse createLoanFromApplicationId(Long loanApplicationId);

  @Transactional(readOnly = true)
  Loan getLoanEntityById(Long loanId);

  LoanResponse getLoanResponseDtoById(Long loanId);

  LoanResponse createLoan(LoanRequest loanRequest);

  List<LoanResponse> getLoansByAccountId(Long accountId);

  List<LoanResponse> filters(long accountId, Long loanTypeId, LocalDate loanRepaymentScheduleFrom,
      LocalDate loanRepaymentScheduleTo, Set<LoanStatus> loanStatus);
}
