package com.app.bankingloanservice.service;

import com.app.bankingloanservice.dto.DocumentResponseDto;
import com.app.bankingloanservice.dto.DocumentUploadDto;
import com.app.bankingloanservice.dto.LoanApplicationRequestDto;
import com.app.bankingloanservice.dto.LoanApplicationResponseDto;
import com.app.bankingloanservice.entity.LoanApplication;

import java.io.IOException;

public interface LoanApplicationService {

    public LoanApplicationResponseDto createLoanApplication(LoanApplicationRequestDto loanApplicationRequestDto) throws IOException;

    LoanApplication getEntityById(Long applicationId);

    LoanApplicationResponseDto getResponseDtoById(Long loanApplicationId);

    DocumentResponseDto uploadLoanApplicationDocument(Long loanApplicationId, DocumentUploadDto documentUploadDto);

    LoanApplicationResponseDto approveApplication(Long applicationId);

    LoanApplicationResponseDto rejectApplication(Long applicationId, String reason);

    LoanApplicationResponseDto requestAdditionalDocuments(Long applicationId, String additionalDocuments);

    LoanApplicationResponseDto startReview(Long applicationId);
}
