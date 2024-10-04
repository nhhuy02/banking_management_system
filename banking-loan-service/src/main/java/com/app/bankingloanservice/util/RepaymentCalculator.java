package com.app.bankingloanservice.util;

import com.app.bankingloanservice.entity.Loan;
import com.app.bankingloanservice.entity.LoanRepayment;

import java.util.List;

public interface RepaymentCalculator {
    List<LoanRepayment> calculateRepaymentSchedule(Loan loan);
}