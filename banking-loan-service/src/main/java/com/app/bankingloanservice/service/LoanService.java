package com.app.bankingloanservice.service;

import com.app.bankingloanservice.dto.LoanRequest;
import com.app.bankingloanservice.dto.LoanResponse;
import com.app.bankingloanservice.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LoanService {

    LoanResponse createLoanFromApplicationId(Long loanApplicationId);

    @Transactional(readOnly = true)
    Loan getLoanEntityById(Long loanId);

    LoanResponse getLoanResponseDtoById(Long loanId);

    LoanResponse createLoan(LoanRequest loanRequest);

    List<LoanResponse> getLoansByAccountId(Long accountId);
}
