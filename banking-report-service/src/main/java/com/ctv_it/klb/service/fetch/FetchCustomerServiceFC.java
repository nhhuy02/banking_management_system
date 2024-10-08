package com.ctv_it.klb.service.fetch;

import com.ctv_it.klb.dto.baseInfo.CustomerInfoDTO;
import com.ctv_it.klb.dto.fetch.response.FetchResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.customer.FetchCustomerDataResponseDTO;
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
      log.info("Fetch customer(accountId={}) completed successfully: \n{}", accountId,
          fetchResponseDTO);

      return handleFetchResponse.handle(fetchResponseDTO);
    } catch (Exception ex) {
      log.error("Fetch customer(accountId={}) failed: \n{}", accountId, ex.toString());
      throw ex;
    }
  }

  public CustomerInfoDTO map(FetchCustomerDataResponseDTO fetchDataResponseDTO) {
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
    return map(findByAccountId(accountId));
  }
}
