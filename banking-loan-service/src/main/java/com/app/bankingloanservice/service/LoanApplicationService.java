package com.app.bankingloanservice.service;

import com.app.bankingloanservice.dto.*;
import com.app.bankingloanservice.entity.LoanApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LoanApplicationService {

    LoanApplicationResponse createLoanApplication(LoanApplicationRequest loanApplicationRequest);

    LoanApplication getEntityById(Long applicationId);

    LoanApplicationResponse getResponseDtoById(Long loanApplicationId);

    List<LoanApplicationResponse> getLoanApplicationsByAccountId(Long accountId);

    DocumentResponse uploadLoanApplicationDocument(Long loanApplicationId, DocumentUploadRequest documentUploadRequest);

    LoanApplicationResponse updateStatus(Long applicationId, LoanApplicationStatusDto loanApplicationStatusDto);
}
