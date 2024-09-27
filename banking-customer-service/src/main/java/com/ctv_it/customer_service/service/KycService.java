package com.ctv_it.customer_service.service;

import com.ctv_it.customer_service.dto.KycDto;
import com.ctv_it.customer_service.dto.KycResponseDto;
import com.ctv_it.customer_service.model.Kyc;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public interface KycService {
    Optional<Kyc.VerificationStatus> getKycStatusById(Long id);

    KycDto updateKycbyAccountId(Long id, KycDto updatedKycDto);

    Optional<KycResponseDto> getKycByCustomerId(Long customerId);

    Kyc saveKyc(Kyc kyc);
}
