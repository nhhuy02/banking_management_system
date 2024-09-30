package com.ojt.klb.repository;

import com.ojt.klb.model.CardRegistrationRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRegistrationRequestRepository extends JpaRepository<CardRegistrationRequest, Long> {
    List<CardRegistrationRequest> findByRequestStatus(CardRegistrationRequest.Status status);

    Optional<CardRegistrationRequest> findByAccountIdAndCardType_CardTypeName(Long accountId, String cardTypeName);

    List<CardRegistrationRequest> findByAccountId(Long accountId);


}
