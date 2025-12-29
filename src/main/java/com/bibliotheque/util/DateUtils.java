package com.bibliotheque.util;

import java.time.LocalDate;

public class DateUtils {

  private DateUtils() {
    // Classe utilitaire
  }

  public static boolean isFutureDate(LocalDate date) {
    if (date == null)
      return false;
    return date.isAfter(LocalDate.now());
  }

  public static boolean isValidBorrowPeriod(LocalDate start, LocalDate end) {
    if (start == null || end == null)
      return false;
    return end.isAfter(start);
  }
}
