package com.ctv_it.klb.util.FakeMockServices;

import com.ctv_it.klb.config.exception.InvalidExceptionCustomize;
import com.ctv_it.klb.dto.base.AccountInfoDTO;
import com.ctv_it.klb.dto.fetch.response.FetchAccountDataDTO;
import com.ctv_it.klb.dto.filter.extend.AccountFilterDTO;
import com.ctv_it.klb.dto.response.ErrorDetailDTO;
import com.ctv_it.klb.util.FilterRangeUtils;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class AccountFetchService {

  public FetchAccountDataDTO filter(Long customerId, AccountFilterDTO accountFilterDTO) {
    List<AccountInfoDTO> filteredAccounts = FakeDataGenerator.initInstance().getAccountResponseDTO()
        .stream()
        .filter(customer -> Objects.equals(customer.getCustomerId(), customerId))
        .flatMap(fa -> fa.getAccounts().stream())
        .filter(acc -> applyFilters(acc, accountFilterDTO))
        .collect(Collectors.toList());

    return FetchAccountDataDTO.builder()
        .customerId(customerId)
        .accounts(filteredAccounts)
        .build();
  }

  public boolean applyFilters(AccountInfoDTO account, AccountFilterDTO filters) {
    if (account == null || filters == null) {
      return true;
    }

    boolean checkOpeningDateRange = Boolean.TRUE;

    if (FilterRangeUtils.isValid(filters.getOpeningDateRange())) {
      checkOpeningDateRange = FilterRangeUtils.isWithin(account.getOpeningDate(),
          filters.getOpeningDateRange());
    } else {
      throw new InvalidExceptionCustomize(
          List.of(ErrorDetailDTO.builder()
              .field("reportFilters.openingDateRange.max")
              .rejectedValue(filters.getOpeningDateRange().getMax())
              .message("max("
                  + filters.getOpeningDateRange().getMax() + ") < min("
                  + filters.getOpeningDateRange().getMin() + ")")
              .build()));
    }

    // Filter by account type
    boolean matchesType = Optional.ofNullable(filters.getAccountType())
        .map(type -> type.equalsIgnoreCase(account.getType()))
        .orElse(true);

    // Filter by account status
    boolean matchesStatus = Optional.ofNullable(filters.getAccountStatus())
        .map(type -> type.equalsIgnoreCase(account.getStatus()))
        .orElse(true);

    return checkOpeningDateRange && matchesType && matchesStatus;
  }

}
