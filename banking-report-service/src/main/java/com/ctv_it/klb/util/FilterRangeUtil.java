package com.ctv_it.klb.util;

import com.ctv_it.klb.config.exception.InvalidExceptionCustomize;
import com.ctv_it.klb.dto.filter.RangeDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;

public class FilterRangeUtil {

  // Check if a LocalDate is within a given RangeDTO<ChronoLocalDate>
  public static boolean isWithin(LocalDate value, RangeDTO<? extends ChronoLocalDate> range) {
    return isWithinChronoLocalDate(value, range);
  }

  // Check if a LocalDateTime is within a given RangeDTO<ChronoLocalDate>
  public static boolean isWithin(LocalDateTime value, RangeDTO<? extends ChronoLocalDate> range) {
    if (value == null) {
      return false;
    }
    return isWithinChronoLocalDate(value.toLocalDate(), range);
  }

  // Generic method to check if a Comparable value is within a given RangeDTO<T>
  public static <T extends Comparable<T>> boolean isWithin(T value, RangeDTO<T> range) {
    return isWithinComparable(value, range);
  }

  // Helper method to check if a ChronoLocalDate value is within a range
  private static boolean isWithinChronoLocalDate(ChronoLocalDate value, RangeDTO<? extends ChronoLocalDate> range) {
    if (value == null) {
      return false;
    }
    if (range == null) {
      return true;
    }

    ChronoLocalDate min = range.getMin();
    ChronoLocalDate max = range.getMax();

    // valid range = both min and max are not null and min is not greater than max
    if (min != null && max != null && min.isAfter(max)) {
      throw new InvalidExceptionCustomize(null);
    }

    boolean isAfterMin = (min == null || !value.isBefore(min));
    boolean isBeforeMax = (max == null || !value.isAfter(max));

    return isAfterMin && isBeforeMax;
  }

  // Helper method to check if a Comparable value is within a range
  private static <T extends Comparable<T>> boolean isWithinComparable(T value, RangeDTO<T> range) {
    if (value == null) {
      return false;
    }
    if (range == null) {
      return true;
    }

    T min = range.getMin();
    T max = range.getMax();

    // valid range = both min and max are not null and min is not greater than max
    if (min != null && max != null && min.compareTo(max) > 0) {
      throw new InvalidExceptionCustomize(null);
    }

    boolean isAfterMin = (min == null || min.compareTo(value) <= 0);
    boolean isBeforeMax = (max == null || max.compareTo(value) >= 0);

    return isAfterMin && isBeforeMax;
  }
}
