package com.app.bankingloanservice.service;

import com.app.bankingloanservice.dto.*;
import com.app.bankingloanservice.entity.LoanApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoanApplicationService {

    LoanApplicationResponse createLoanApplication(LoanApplicationRequest loanApplicationRequest);

    LoanApplication getEntityById(Long applicationId);

    LoanApplicationResponse getResponseDtoById(Long loanApplicationId);

    Page<LoanApplicationResponse> getLoanApplicationsByCustomerId(Long customerId, Pageable pageable);

    DocumentResponse uploadLoanApplicationDocument(Long loanApplicationId, DocumentUploadRequest documentUploadRequest);

    LoanApplicationResponse updateStatus(Long applicationId, LoanApplicationStatusDto loanApplicationStatusDto);
}
