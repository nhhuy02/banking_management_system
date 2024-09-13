package com.ctv_it.customer_service.repository;

import com.ctv_it.customer_service.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, String> {
    Optional<VerificationCode> findByCustomerIdAndCode(Long customerId, String code);
}
