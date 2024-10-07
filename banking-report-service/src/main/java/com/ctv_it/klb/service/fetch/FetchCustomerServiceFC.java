package com.ctv_it.klb.service.fetch;

import com.ctv_it.klb.dto.baseInfo.CustomerInfoDTO;
import com.ctv_it.klb.dto.fetch.response.FetchResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchCustomerDataResponseDTO;
import com.ctv_it.klb.feignClient.CustomerServiceFC;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FetchCustomerServiceFC {

  private final CustomerServiceFC customerServiceFC;
  private final HandleFetchResponse<FetchCustomerDataResponseDTO> handleFetchResponse;

  public FetchCustomerDataResponseDTO findByAccountId(long accountId) {
    try {
      log.info("Fetch customer(accountId={}) is processing", accountId);
      FetchResponseDTO<FetchCustomerDataResponseDTO> fetchResponseDTO = customerServiceFC.findByAccountId(
          accountId);
      log.info("Fetch customer(accountId={}) passed: {}", accountId, fetchResponseDTO);

      return handleFetchResponse.handle(fetchResponseDTO);
    } catch (Exception ex) {
      log.error("Fetch customer(accountId={}) failed: {}", accountId, ex.toString());
      throw ex;
    }
  }

  public CustomerInfoDTO map(FetchCustomerDataResponseDTO fetchDataResponseDTO) {
    log.debug("Mapping FetchCustomerDataDTO to CustomerInfoDTO: {}", fetchDataResponseDTO);
    return CustomerInfoDTO.builder()
        .id(fetchDataResponseDTO.getCustomerId())
        .fullName(fetchDataResponseDTO.getFullName())
        .email(fetchDataResponseDTO.getEmail())
        .phoneNumber(fetchDataResponseDTO.getPhoneNumber())
        .address(fetchDataResponseDTO.getCurrentAddress())
        .dateOfBirth(fetchDataResponseDTO.getDateOfBirth().format(
            DateTimeFormatter.ofPattern("dd-MM-yyyy")))
        .kycDocumentType(fetchDataResponseDTO.getKyc().getDocumentType())
        .kycDocumentNumber(fetchDataResponseDTO.getKyc().getDocumentNumber())
        .build();
  }

  public CustomerInfoDTO fetchCustomerByAccountId(long accountId) {
    log.info("Fetching customer data for accountId: {}", accountId);
    FetchCustomerDataResponseDTO data = findByAccountId(accountId);
    CustomerInfoDTO customer = map(data);
    log.info("Mapped customer data: {}", customer);
    return customer;
  }
}
