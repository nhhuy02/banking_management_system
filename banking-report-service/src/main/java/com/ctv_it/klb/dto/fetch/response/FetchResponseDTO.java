package com.ctv_it.klb.dto.fetch.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FetchResponseDTO<T> {

  private boolean success;
  private int status;
  private String message;
  private T data;
}
