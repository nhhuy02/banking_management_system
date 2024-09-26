package com.ctv_it.klb.dto.filter;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class RangeDTO<T> implements Serializable {

  private T min;
  private T max;
}
