package com.ctv_it.klb.dto.fetch.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class FetchResponseDTO<T> {

  private boolean success;
  private int status;
  private String message;
  private T data;
}
