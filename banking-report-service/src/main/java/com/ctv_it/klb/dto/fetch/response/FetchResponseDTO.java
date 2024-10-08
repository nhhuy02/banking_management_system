package com.ctv_it.klb.dto.fetch.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class FetchResponseDTO<T> {

  private boolean success;
  private int status;
  private String message;
  private T data;
  private Object details;

}
