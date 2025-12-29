package com.bibliotheque;

import com.bibliotheque.util.DatabaseConnection;

public class MainTest {

  public static void main(String[] args) {
    try {
      DatabaseConnection db = DatabaseConnection.getInstance();
      System.out.println("Database connection SUCCESS");
    } catch (Exception e) {
      System.out.println("Database connection FAILED");
      e.printStackTrace();
    }
  }
}
