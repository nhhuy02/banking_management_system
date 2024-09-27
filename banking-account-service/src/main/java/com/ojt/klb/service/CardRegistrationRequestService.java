package com.ojt.klb.service;

import com.ojt.klb.dto.CardRegistrationRequestDto;
import com.ojt.klb.dto.CardRegistrationRequestResponseDto;
import com.ojt.klb.dto.CardRegistrationRequestUpdateDto;
import com.ojt.klb.model.CardRegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface CardRegistrationRequestService {
    Optional<CardRegistrationRequestDto> registerCard(Long accountId,CardRegistrationRequestDto cardRegistrationRequestDto);
    Optional<List<CardRegistrationRequestResponseDto>> getAllCardRegistrationRequestsStatusPending();
    void updateCardRegistrationRequestStatusPending(Long accountId, CardRegistrationRequestUpdateDto dto);
    Optional<List<CardRegistrationRequestResponseDto>> getAllCardRegistrationRequestsByAccountId(Long accountId);
}
