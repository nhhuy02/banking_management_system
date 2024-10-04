package com.ojt.klb.service.impl;

import com.ojt.klb.external.TransactionClient;
import com.ojt.klb.model.TransactionStatus;
import com.ojt.klb.model.dto.UtilityPaymentDto;
import com.ojt.klb.model.entity.UtilityPayment;
import com.ojt.klb.model.mapper.UtilityPaymentMapper;
import com.ojt.klb.model.request.UtilityPaymentRequest;
import com.ojt.klb.model.response.UtilityPaymentResponse;
import com.ojt.klb.repository.UtilityPaymentRepository;
import com.ojt.klb.service.UtilityPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UtilityPaymentServiceImpl implements UtilityPaymentService {
    private final UtilityPaymentRepository utilityPaymentRepository;
    private final TransactionClient transactionClient;

    private UtilityPaymentMapper utilityPaymentMapper = new UtilityPaymentMapper();

    @Override
    public UtilityPaymentResponse utilPayment(UtilityPaymentRequest paymentRequest) {
        log.info("Utility payment processing {}", paymentRequest.toString());

        UtilityPayment utilityPayment = new UtilityPayment();
        BeanUtils.copyProperties(paymentRequest, utilityPayment);
        utilityPayment.setStatus(TransactionStatus.PROCESSING);

        UtilityPayment saveUtilityPayment = utilityPaymentRepository.save(utilityPayment);

        UtilityPaymentResponse utilityPaymentResponse = transactionClient.utilityPayment(paymentRequest);
        log.info("Transaction response {}", utilityPaymentResponse.toString());

        saveUtilityPayment.setStatus(TransactionStatus.SUCCESS);
        saveUtilityPayment.setReferenceNumber(utilityPaymentResponse.getReferenceNumber());
        utilityPaymentRepository.save(saveUtilityPayment);

        return UtilityPaymentResponse.builder()
                .message("Utility Payment Successfully Processed")
                .referenceNumber(utilityPaymentResponse.getReferenceNumber())
                .build();
    }

    @Override
    public List<UtilityPaymentDto> readPayments(Pageable pageable) {
        Page<UtilityPayment> allUtilPayments = utilityPaymentRepository.findAll(pageable);
        return utilityPaymentMapper.convertToDtoList(allUtilPayments.getContent());
    }
}
