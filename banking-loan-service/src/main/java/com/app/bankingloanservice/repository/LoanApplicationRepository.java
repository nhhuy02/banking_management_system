package com.app.bankingloanservice.repository;

import com.app.bankingloanservice.entity.LoanApplication;
import com.app.bankingloanservice.constant.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {

    // Find a loan application by its ID
    Optional<LoanApplication> findByLoanApplicationId(Long id);

    /**
     * Find loan applications based on customerId with pagination support.
     *
     * @param customerId The customer's ID.
     * @param pageable Paging information.
     * @return Loan applications page.
     */
    Page<LoanApplication> findByCustomerId(Long customerId, Pageable pageable);

    /**
     * Find all loan applications by their application status.
     *
     * @param applicationStatus The status of the loan applications to find.
     * @return A list of loan applications with the given status.
     */
    List<LoanApplication> findByApplicationStatus(ApplicationStatus applicationStatus);

    // Find loan applications by loan type ID
    List<LoanApplication> findByLoanType_LoanTypeId(Long loanTypeId);

    // Find loan applications by desired disbursement date
    List<LoanApplication> findByDesiredDisbursementDate(LocalDate desiredDisbursementDate);

    // Custom query to find loan applications by a range of submission dates
    @Query("SELECT la FROM LoanApplication la WHERE la.submissionDate BETWEEN :startDate AND :endDate")
    List<LoanApplication> findBySubmissionDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Custom query to find loan applications that require documents
    @Query("SELECT la FROM LoanApplication la WHERE la.documents IS NOT EMPTY")
    List<LoanApplication> findApplicationsWithDocuments();

    // Custom query to find loan applications that have been reviewed but not yet approved
    @Query("SELECT la FROM LoanApplication la WHERE la.reviewDate IS NOT NULL AND la.applicationStatus <> 'APPROVED'")
    List<LoanApplication> findReviewedNotApprovedApplications();

}
