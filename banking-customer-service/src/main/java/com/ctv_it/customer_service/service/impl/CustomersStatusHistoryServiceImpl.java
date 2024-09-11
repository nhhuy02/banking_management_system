package com.ctv_it.customer_service.service.impl;

import com.ctv_it.customer_service.dto.CustomersStatusHistoryDto;
import com.ctv_it.customer_service.mapper.CustomersStatusHistoryMapper;
import com.ctv_it.customer_service.model.CustomersStatusHistory;
import com.ctv_it.customer_service.repository.CustomersStatusHistoryRepository;
import com.ctv_it.customer_service.service.CustomersStatusHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomersStatusHistoryServiceImpl implements CustomersStatusHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(CustomersStatusHistoryServiceImpl.class);

    @Autowired
    private CustomersStatusHistoryRepository repository;

    @Autowired
    private CustomersStatusHistoryMapper mapper;

    @Override
    public Optional<CustomersStatusHistoryDto> getLatestStatusByCustomerId(Long customerId) {
        logger.info("Fetching latest status for customer ID: {}", customerId);
        return repository.findLatestByCustomerId(customerId)
                .map(mapper::toDto)
                .or(() -> {
                    logger.warn("No status found for customer ID: {}", customerId);
                    return Optional.empty();
                });
    }

    @Override
    public Optional<List<CustomersStatusHistoryDto>> getAllStatusByCustomerId(Long customerId) {
        logger.info("Fetching all statuses for customer ID: {}", customerId);
        List<CustomersStatusHistory> entities = repository.findAllByCustomerId(customerId);
        if (entities.isEmpty()) {
            logger.warn("No statuses found for customer ID: {}", customerId);
            return Optional.empty();
        } else {
            List<CustomersStatusHistoryDto> dtoList = entities.stream()
                    .map(mapper::toDto)
                    .peek(dto -> logger.debug("Mapped DTO: {}", dto))
                    .collect(Collectors.toList());
            logger.info("Found {} statuses for customer ID: {}", dtoList.size(), customerId);
            return Optional.of(dtoList);
        }
    }

    @Override
    public CustomersStatusHistory saveStatusHistory(CustomersStatusHistory statusHistory) {
        return repository.save(statusHistory);
    }
}
