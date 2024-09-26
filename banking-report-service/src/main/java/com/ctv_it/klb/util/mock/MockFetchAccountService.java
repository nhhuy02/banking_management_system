package com.ctv_it.klb.util.mock;

import com.ctv_it.klb.config.exception.InvalidExceptionCustomize;
import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.baseInfo.AccountInfoDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchAccountDataDTO;
import com.ctv_it.klb.dto.filter.extend.AccountFilterDTO;
import com.ctv_it.klb.dto.response.ErrorDetailDTO;
import com.ctv_it.klb.util.FilterRangeUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MockFetchAccountService {

  public FetchAccountDataDTO filter(Long customerId, AccountFilterDTO accountFilterDTO) {
    return FetchAccountDataDTO.builder().build();
  }

  public boolean applyFilters(AccountInfoDTO account, AccountFilterDTO filters) {
    if (account == null || filters == null) {
      return true;
    }

    boolean checkOpeningDateRange = Boolean.TRUE;

    // check range
    try {
      checkOpeningDateRange = FilterRangeUtil.isWithin(
          account.getOpeningDate(),
          filters.getOpeningDateRange());
    } catch (InvalidExceptionCustomize ex) {
      throw new InvalidExceptionCustomize(
          Collections.singletonList(
              ErrorDetailDTO.builder()
                  .field("filters.range.max")
                  .rejectedValue(filters.getOpeningDateRange().getMax())
                  .message(
                      Translator.toLocale(
                          "error.invalid.range-3",
                          filters.getOpeningDateRange().getMax(),
                          "<",
                          filters.getOpeningDateRange().getMin())
                  ).build()));
    }

    // check type
    boolean matchesType = Optional.ofNullable(filters.getAccountType())
        .map(type -> type.equalsIgnoreCase(account.getType()))
        .orElse(true);

    // check status
    boolean matchesStatus = Optional.ofNullable(filters.getAccountStatus())
        .map(type -> type.equalsIgnoreCase(account.getStatus()))
        .orElse(true);

    return checkOpeningDateRange && matchesType && matchesStatus;
  }
}
