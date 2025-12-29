package com.bibliotheque;

import com.bibliotheque.util.StringValidator;
import com.bibliotheque.util.DateUtils;

import java.time.LocalDate;

public class TestModel {

  public static void main(String[] args) {

    System.out.println("===== TEST DES VALIDATIONS =====");

    // Test Email
    System.out.println("Email valide (test@mail.com) : "
        + StringValidator.isValidEmail("test@mail.com"));

    System.out.println("Email invalide (test@mail) : "
        + StringValidator.isValidEmail("test@mail"));

    // Test ISBN
    System.out.println("ISBN valide (9780134685991) : "
        + StringValidator.isValidISBN("9780134685991"));

    System.out.println("ISBN invalide (1234) : "
        + StringValidator.isValidISBN("1234"));

    // Test Dates
    LocalDate today = LocalDate.now();
    LocalDate future = today.plusDays(7);

    System.out.println("Date future valide : "
        + DateUtils.isFutureDate(future));

    System.out.println("Période d'emprunt valide : "
        + DateUtils.isValidBorrowPeriod(today, future));

    System.out.println("===== FIN DES TESTS =====");
  }
}
