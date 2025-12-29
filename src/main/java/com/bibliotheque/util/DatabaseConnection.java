package com.bibliotheque.util;

import java.lang.RuntimeException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class DatabaseConnection {
  // 1. Instance unique statique
  private static DatabaseConnection instance;
  // 2. Connexion à partager
  private Connection connection;

  // 3. Créer la connexion
  private DatabaseConnection() {
    try {
      Properties props = new Properties();

      InputStream input = getClass()
          .getClassLoader()
          .getResourceAsStream("config.properties");

      if (input == null) {
        throw new RuntimeException("config.properties not found");
      }

      props.load(input);

      String url = props.getProperty("db.url");
      String user = props.getProperty("db.user");
      String password = props.getProperty("db.password");

      connection = DriverManager.getConnection(url, user, password);

    } catch (IOException | SQLException e) {
      throw new RuntimeException("Database connection failed", e);
    }
  }

  // 4. Méthode publique pour obtenir l'instance
  public static DatabaseConnection getInstance() {
    if (instance == null) {
      synchronized (DatabaseConnection.class) {
        if (instance == null) {
          instance = new DatabaseConnection();
        }
      }
    }
    return instance;
  }

  public Connection getConnection() {
    return connection;
  }

  public void close() {
    try {
      if (connection != null && !connection.isClosed()) {
        connection.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
