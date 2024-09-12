package com.ctv_it.customer_service.service;

import com.ctv_it.customer_service.dto.CustomersStatusHistoryDto;
import com.ctv_it.customer_service.model.CustomersStatusHistory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CustomersStatusHistoryService {

    Optional<CustomersStatusHistoryDto> getLatestStatusByCustomerId(Long customerId);

    Optional<List<CustomersStatusHistoryDto>> getAllStatusByCustomerId(Long customerId);

    CustomersStatusHistory saveStatusHistory(CustomersStatusHistory statusHistory);

}

