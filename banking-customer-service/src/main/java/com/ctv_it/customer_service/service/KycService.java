package com.ctv_it.customer_service.service;

import com.ctv_it.customer_service.dto.KycDto;
import com.ctv_it.customer_service.dto.KycResponseDto;
import com.ctv_it.customer_service.model.Kyc;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public interface KycService {
    Optional<Kyc.VerificationStatus> getKycStatusById(Long id);

    KycDto updateKyc(Long id, KycDto updatedKycDto);

    Optional<KycResponseDto> getKycById(Long id);

    Kyc saveKyc(Kyc kyc);
}
