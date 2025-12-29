package com.bibliotheque.util;

import java.util.regex.Pattern;

public class StringValidator {

  // Regex email simple et correcte pour le projet
  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

  // ISBN-10 ou ISBN-13 (chiffres + tirets autorisés)
  private static final Pattern ISBN_PATTERN = Pattern.compile("^(97(8|9))?\\d{9}(\\d|X)$");

  private StringValidator() {
    // Empêche l'instanciation (classe utilitaire)
  }

  public static boolean isNullOrEmpty(String value) {
    return value == null || value.trim().isEmpty();
  }

  public static boolean isValidEmail(String email) {
    return email != null && EMAIL_PATTERN.matcher(email).matches();
  }

  public static boolean isValidISBN(String isbn) {
    if (isNullOrEmpty(isbn))
      return false;
    String cleanIsbn = isbn.replace("-", "");
    return ISBN_PATTERN.matcher(cleanIsbn).matches();
  }
}
