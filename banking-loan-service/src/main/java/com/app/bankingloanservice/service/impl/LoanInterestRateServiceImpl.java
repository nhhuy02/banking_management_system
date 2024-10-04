package com.app.bankingloanservice.service.impl;

import com.app.bankingloanservice.dto.LoanInterestRateRequest;
import com.app.bankingloanservice.entity.Loan;
import com.app.bankingloanservice.entity.LoanInterestRate;
import com.app.bankingloanservice.exception.LoanInterestRateNotFoundException;
import com.app.bankingloanservice.exception.LoanNotFoundException;
import com.app.bankingloanservice.mapper.LoanInterestRateMapper;
import com.app.bankingloanservice.repository.LoanInterestRateRepository;
import com.app.bankingloanservice.repository.LoanRepository;
import com.app.bankingloanservice.service.LoanInterestRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LoanInterestRateServiceImpl implements LoanInterestRateService {

    private final LoanInterestRateRepository loanInterestRateRepository;

    private final LoanInterestRateMapper loanInterestRateMapper;

    private final LoanRepository loanRepository;

    // Create an interest rate
    @Override
    public LoanInterestRate createLoanInterestRate(Long loanId, LoanInterestRateRequest loanInterestRateRequest) {
        log.info("Saving LoanInterestRate for loan ID: {}", loanId);

        // Mapping from Dto to Entity
        LoanInterestRate loanInterestRate = loanInterestRateMapper.toEntity(loanInterestRateRequest);

        // Get loan:
        log.info("Attempting to get loan with ID: {}", loanId);

        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> {
            String errorMessage = String.format("Loan with ID %d not found. Please check the ID and try again.", loanId);
            log.error(errorMessage);
            return new LoanNotFoundException(errorMessage);
        });

        // Set Loan to new LoanInterestRate
        loanInterestRate.setLoan(loan);

        // Validation: Ensure there is no overlap in interest rate periods for the same loan
//        validateInterestRatePeriods(loanInterestRate);

        LoanInterestRate savedRate = loanInterestRateRepository.save(loanInterestRate);
        log.info("LoanInterestRate saved with ID: {}", savedRate.getLoanInterestRateId());
        return savedRate;
    }


    @Override
    public LoanInterestRate updateEffectiveToDate(Long loanInterestRateId, LocalDate effectiveTo) {
        log.info("Updating effectiveTo date for LoanInterestRate ID: {}", loanInterestRateId);

        // Find LoanInterestRate by ID
        LoanInterestRate existingRate = loanInterestRateRepository.findById(loanInterestRateId)
                .orElseThrow(() -> {
                    log.error("LoanInterestRate with ID: {} not found", loanInterestRateId);
                    return new LoanInterestRateNotFoundException("LoanInterestRate with ID: " + loanInterestRateId + " not found.");
                });

        // Update the effectiveTo date
        existingRate.setEffectiveTo(effectiveTo);

        // Save the changes
        LoanInterestRate updatedRate = loanInterestRateRepository.save(existingRate);

        log.info("Updated effectiveTo date for LoanInterestRate ID: {} to {}", loanInterestRateId, effectiveTo);
        return updatedRate;
    }


    // Retrieve interest rate history for a specific loan
    @Override
    public List<LoanInterestRate> getInterestRateHistory(Long loanId) {
        log.info("Fetching interest rate history for loan ID: {}", loanId);
        List<LoanInterestRate> rates = loanInterestRateRepository.findByLoanLoanId(loanId);
        if (rates.isEmpty()) {
            log.warn("No interest rate history found for loan ID: {}", loanId);
            throw new LoanInterestRateNotFoundException("No interest rate history found for loan ID: " + loanId);
        }
        log.info("Found {} interest rate records for loan ID: {}", rates.size(), loanId);
        return rates;
    }

    // Get the current interest rate for a loan at a given date
    @Override
    public LoanInterestRate getCurrentInterestRate(Long loanId, LocalDate date) {
        log.info("Fetching current interest rate for loan ID: {} on date: {}", loanId, date);
        LoanInterestRate rate = loanInterestRateRepository.findCurrentInterestRateByLoanIdAndDate(loanId, date);
        if (rate == null) {
            log.error("No current interest rate found for loan ID: {} on date: {}", loanId, date);
            throw new LoanInterestRateNotFoundException("No current interest rate found for loan ID: " + loanId + " on date: " + date);
        }
        log.info("Current interest rate found: {} for loan ID: {}", rate.getAnnualInterestRate(), loanId);
        return rate;
    }

    // Get interest rates within a specified date range
    @Override
    public List<LoanInterestRate> getInterestRatesInDateRange(Long loanId, LocalDate startDate, LocalDate endDate) {
        log.info("Fetching interest rates for loan ID: {} between dates: {} and {}", loanId, startDate, endDate);
        List<LoanInterestRate> rates = loanInterestRateRepository.findInterestRatesInDateRange(loanId, startDate, endDate);
        if (rates.isEmpty()) {
            log.warn("No interest rates found for loan ID: {} between dates: {} and {}", loanId, startDate, endDate);
            throw new LoanInterestRateNotFoundException("No interest rates found for loan ID: " + loanId + " in the specified date range.");
        }
        log.info("Found {} interest rate records for loan ID: {} in the specified date range", rates.size(), loanId);
        return rates;
    }

    // Delete an interest rate record by its ID
    @Override
    public void deleteLoanInterestRate(Long loanInterestRateId) {
        log.info("Deleting LoanInterestRate with ID: {}", loanInterestRateId);
        if (!loanInterestRateRepository.existsById(loanInterestRateId)) {
            log.error("LoanInterestRate with ID: {} not found", loanInterestRateId);
            throw new LoanInterestRateNotFoundException("LoanInterestRate with ID: " + loanInterestRateId + " not found.");
        }
        loanInterestRateRepository.deleteById(loanInterestRateId);
        log.info("LoanInterestRate with ID: {} has been deleted", loanInterestRateId);
    }

//    // Validation to ensure there is no overlap in interest rate periods for the same loan
//    private void validateInterestRatePeriods(LoanInterestRate loanInterestRate) {
//
//        log.info("Starting to validate Loan Interest Rate: {}", loanInterestRate);
//
//        List<LoanInterestRate> existingRates = loanInterestRateRepository.findInterestRatesInDateRange(
//                loanInterestRate.getLoan().getLoanId(),
//                loanInterestRate.getEffectiveFrom(),
//                loanInterestRate.getEffectiveTo() == null ? LocalDate.now() : loanInterestRate.getEffectiveTo()
//        );
//
//        if (!existingRates.isEmpty()) {
//            log.error("Interest rate period overlap detected for loan ID: {}", loanInterestRate.getLoan().getLoanId());
//            throw new InterestRatePeriodOverlapException("Interest rate period overlaps with an existing rate for loan ID: " + loanInterestRate.getLoan().getLoanId());
//        }
//    }
}
