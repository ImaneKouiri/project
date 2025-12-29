package com.bibliotheque.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;

public class MainController {

  @FXML
  private TabPane tabPane;

  @FXML
  public void initialize() {
    // Appelé automatiquement au chargement du FXML
    System.out.println("MainController chargé avec succès");
  }
}
