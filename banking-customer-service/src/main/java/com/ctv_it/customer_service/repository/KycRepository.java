package com.ctv_it.customer_service.repository;

import com.ctv_it.customer_service.model.Kyc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KycRepository extends JpaRepository<Kyc, Long> {
    boolean existsByDocumentNumber(String documentNumber);
}
