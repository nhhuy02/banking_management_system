package com.ctv_it.klb.dto.filter;

import com.ctv_it.klb.config.validation.FieldName;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RangeDTO<T> implements Serializable {

  @FieldName("min")
  private T min;
  @FieldName("max")
  private T max;
}
