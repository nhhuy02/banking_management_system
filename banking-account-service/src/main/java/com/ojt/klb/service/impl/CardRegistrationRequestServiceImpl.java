package com.ojt.klb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ojt.klb.dto.CardRegistrationRequestDto;
import com.ojt.klb.dto.CardRegistrationRequestResponseDto;
import com.ojt.klb.dto.CardRegistrationRequestUpdateDto;
import com.ojt.klb.model.Account;
import com.ojt.klb.model.Card;
import com.ojt.klb.model.CardRegistrationRequest;
import com.ojt.klb.model.CardType;
import com.ojt.klb.repository.AccountRepository;
import com.ojt.klb.repository.CardRegistrationRequestRepository;
import com.ojt.klb.repository.CardRepository;
import com.ojt.klb.repository.CardTypeRepository;
import com.ojt.klb.service.CardRegistrationRequestService;
import com.ojt.klb.utils.GenerateUniqueNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class CardRegistrationRequestServiceImpl implements CardRegistrationRequestService {

    private static final Logger logger = LoggerFactory.getLogger(CardRegistrationRequestServiceImpl.class);


    private final CardRegistrationRequestRepository cardRegistrationRequestRepository;

    private final AccountRepository accountRepository;

    private final CardTypeRepository cardTypeRepository;

    private final CardRepository cardRepository;

    private final GenerateUniqueNumber generateUniqueNumber;

    private final RestTemplate restTemplate;

    private static final String CUSTOMER_SERVICE_URL = "http://localhost:8082/api/v1/customer/";

    private final KafkaTemplate<String, CardRegistrationRequestUpdateDto> kafkaTemplate;

    private static final String TOPIC = "send-result-register-card-topic";

    public CardRegistrationRequestServiceImpl(CardRegistrationRequestRepository cardRegistrationRequestRepository, AccountRepository accountRepository, CardTypeRepository cardTypeRepository, CardRepository cardRepository, GenerateUniqueNumber generateUniqueNumber, RestTemplate restTemplate, KafkaTemplate<String, CardRegistrationRequestUpdateDto> kafkaTemplate) {
        this.cardRegistrationRequestRepository = cardRegistrationRequestRepository;
        this.accountRepository = accountRepository;
        this.cardTypeRepository = cardTypeRepository;
        this.cardRepository = cardRepository;
        this.generateUniqueNumber = generateUniqueNumber;
        this.restTemplate = restTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Optional<CardRegistrationRequestDto> registerCard(Long accountId, CardRegistrationRequestDto cardRegistrationRequestDto) {
        try {
            Account account = accountRepository.findById(accountId).orElseThrow(() -> {
                logger.error("Account not found for accountId: {}", accountId);
                return new RuntimeException("Account not found");
            });

            CardType cardType = cardTypeRepository.findByCardTypeName(cardRegistrationRequestDto.getCardTypeName());
            if (cardType == null) {
                logger.error("CardType not found for name: {}", cardRegistrationRequestDto.getCardTypeName());
                throw new RuntimeException("CardType not found");
            }

            CardRegistrationRequest cardRegistrationRequest = new CardRegistrationRequest();
            cardRegistrationRequest.setAccount(account);
            cardRegistrationRequest.setCardType(cardType);
            cardRegistrationRequest.setRequestStatus(CardRegistrationRequest.Status.pending);
            cardRegistrationRequest.setRequestDate(Timestamp.from(Instant.now()));

            cardRegistrationRequestRepository.save(cardRegistrationRequest);
            return Optional.of(cardRegistrationRequestDto);

        } catch (Exception e) {
            logger.error("Error registering card for accountId: {}: {}", accountId, e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<CardRegistrationRequestResponseDto>> getAllCardRegistrationRequestsStatusPending() {
        List<CardRegistrationRequest> cardRegistrationRequestList = cardRegistrationRequestRepository.findByRequestStatus(CardRegistrationRequest.Status.pending);

        if (!cardRegistrationRequestList.isEmpty()) {
            List<CardRegistrationRequestResponseDto> responseDtoList = new ArrayList<>();

            for (CardRegistrationRequest request : cardRegistrationRequestList) {
                CardRegistrationRequestResponseDto dto = new CardRegistrationRequestResponseDto();

                Account account = request.getAccount();
                if (account != null) {
                    dto.setAccountNumber(String.valueOf(account.getAccountNumber()));
                } else {
                    logger.error("Account not found for request with id: {}", request.getId());
                    continue;
                }

                String url = CUSTOMER_SERVICE_URL + account.getId();
                try {
                    callDataApi(dto, account, url);
                } catch (Exception e) {
                    logger.error("Error fetching account data for accountId: {}", account.getId(), e);
                    continue;
                }

                cardType(responseDtoList, request, dto, account);
            }

            return Optional.of(responseDtoList);
        }
        return Optional.empty();
    }

    private void cardType(List<CardRegistrationRequestResponseDto> responseDtoList, CardRegistrationRequest request, CardRegistrationRequestResponseDto dto, Account account) {
        CardType cardType = cardTypeRepository.findById(Long.valueOf(request.getCardType().getId()))
                .orElseThrow(() -> new RuntimeException("Card type not found for id: " + request.getCardType().getId()));
        dto.setCardTypeName(cardType.getCardTypeName());

        dto.setAccountId(account.getId());
        dto.setStatus(request.getRequestStatus().name());
        dto.setRequestDate(request.getRequestDate().toLocalDateTime().toLocalDate());
        dto.setReviewDate(request.getReviewDate() != null ? request.getReviewDate().toLocalDateTime().toLocalDate() : null);
        dto.setNotes(request.getNotes());

        responseDtoList.add(dto);
    }

    private void callDataApi(CardRegistrationRequestResponseDto dto, Account account, String url) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            CardRegistrationRequestResponseDto parsedData = parseData(responseEntity.getBody());
            if (parsedData != null) {
                dto.setName(parsedData.getName());
                dto.setEmail(parsedData.getEmail());
                dto.setPhone(parsedData.getPhone());
            }
        } else {
            logger.error("xError fetching account data for accountId: {} - Status code: {}", account.getId(), responseEntity.getStatusCode());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateCardRegistrationRequestStatusPending(Long accountId, CardRegistrationRequestUpdateDto dto) {
        Optional<CardRegistrationRequest> cardRegistrationRequestOptional =
                cardRegistrationRequestRepository.findByAccountIdAndCardType_CardTypeName(accountId, dto.getCardTypeName());

        if (cardRegistrationRequestOptional.isEmpty()) {
            logger.error("Card registration request not found for accountId: {}", accountId);
            return;
        }

        CardRegistrationRequest cardRegistrationRequest = cardRegistrationRequestOptional.get();

        if (!cardRegistrationRequest.getCardType().getCardTypeName().equals(dto.getCardTypeName())) {
            logger.error("Card type mismatch for accountId: {}. Expected: {}, but found: {}",
                    accountId, dto.getCardTypeName(), cardRegistrationRequest.getCardType().getCardTypeName());
            return;
        }

        Account account = cardRegistrationRequest.getAccount();

        String url = CUSTOMER_SERVICE_URL + accountId;
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                CardRegistrationRequestResponseDto parsedData = parseData(responseEntity.getBody());
                if (parsedData != null) {
                    dto.setEmail(parsedData.getEmail());
                }
            } else {
                logger.error("Error fetching account data for accountId: {} - Status code: {}", accountId, responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Error fetching account data for accountId: {}", accountId, e);
        }

        cardRegistrationRequest.setRequestStatus(CardRegistrationRequest.Status.valueOf(dto.getStatus()));
        cardRegistrationRequest.setNotes(dto.getNotes());
        cardRegistrationRequest.setReviewDate(Timestamp.from(Instant.now()));
        cardRegistrationRequestRepository.save(cardRegistrationRequest);

        CardRegistrationRequestUpdateDto kafkaData = new CardRegistrationRequestUpdateDto();
        kafkaData.setStatus(cardRegistrationRequest.getRequestStatus().name());
        kafkaData.setNotes(cardRegistrationRequest.getNotes());
        kafkaData.setEmail(dto.getEmail());

        if (cardRegistrationRequest.getRequestStatus() == CardRegistrationRequest.Status.approved) {
            Card card = new Card();
            card.setAccount(account);
            card.setCardNumber(generateUniqueNumber.generateCardNumber());
            card.setCardType(cardRegistrationRequest.getCardType());
            card.setBalance(BigDecimal.ZERO);
            card.setStatus(Card.Status.active);
            card.setOpenedAt(Timestamp.from(Instant.now()));

            cardRepository.save(card);

            logger.info("Created card successfully for accountId: {} and cardType: {}", accountId, cardRegistrationRequest.getCardType().getCardTypeName());

            try {
                kafkaTemplate.send(TOPIC, kafkaData);
                logger.info("Sent approved customer information to Kafka topic: {}", TOPIC);
            } catch (Exception e) {
                logger.error("Failed to send approved message to Kafka for accountId: {}", accountId, e);
            }

        } else if (cardRegistrationRequest.getRequestStatus() == CardRegistrationRequest.Status.rejected) {
            try {
                kafkaTemplate.send(TOPIC, kafkaData);
                logger.info("Sent rejected customer information to Kafka topic: {}", TOPIC);
            } catch (Exception e) {
                logger.error("Failed to send rejected message to Kafka for accountId: {}", accountId, e);
            }

        } else {
            logger.error("Invalid request status: {}", dto.getStatus());
        }
    }

    @Override
    public Optional<List<CardRegistrationRequestResponseDto>> getAllCardRegistrationRequestsByAccountId(Long accountId) {
        List<CardRegistrationRequest> cardRegistrationRequestList = cardRegistrationRequestRepository.findByAccountId(accountId);

        if (!cardRegistrationRequestList.isEmpty()) {
            List<CardRegistrationRequestResponseDto> responseDtoList = new ArrayList<>();

            for (CardRegistrationRequest request : cardRegistrationRequestList) {
                CardRegistrationRequestResponseDto dto = new CardRegistrationRequestResponseDto();

                Account account = request.getAccount();
                if (account != null) {
                    dto.setAccountNumber(String.valueOf(account.getAccountNumber()));
                } else {
                    logger.error("xAccount not found for request with id: {}", request.getId());
                    continue;
                }

                String url = CUSTOMER_SERVICE_URL + account.getId();
                try {
                    callDataApi(dto, account, url);
                } catch (Exception e) {
                    logger.error("Error fetching account data for accountId: {}", account.getId(), e);
                }

                cardType(responseDtoList, request, dto, account);
            }

            return Optional.of(responseDtoList);
        }
        return Optional.empty();
    }


    private CardRegistrationRequestResponseDto parseData(String responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode dataNode = rootNode.path("data");

            if (dataNode.isMissingNode()) {
                logger.error("Missing 'data' node in the response: {}", responseBody);
                return null;
            }

            CardRegistrationRequestResponseDto dto = new CardRegistrationRequestResponseDto();
            dto.setName(dataNode.path("fullName").asText());
            dto.setEmail(dataNode.path("email").asText());
            dto.setPhone(dataNode.path("phoneNumber").asText());

            return dto;
        } catch (Exception e) {
            logger.error("Error parsing customer data: ", e);
            return null;
        }
    }
}