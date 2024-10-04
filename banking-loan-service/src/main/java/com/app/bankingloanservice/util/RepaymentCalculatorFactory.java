package com.app.bankingloanservice.util;

import com.app.bankingloanservice.constant.RepaymentMethod;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RepaymentCalculatorFactory {

    private final EqualInstallmentsRepaymentCalculator equalInstallmentsRepaymentCalculator;

    private final ReducingBalanceRepaymentCalculator reducingBalanceRepaymentCalculator;

    public RepaymentCalculator getRepaymentCalculator(RepaymentMethod method) {
        return switch (method) {
            case EQUAL_INSTALLMENTS -> equalInstallmentsRepaymentCalculator;
            case REDUCING_BALANCE -> reducingBalanceRepaymentCalculator;
            default -> throw new IllegalArgumentException("Unsupported Repayment Method: " + method);
        };
    }
}