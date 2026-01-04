package com.bibliotheque.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class MainController {

  @FXML
  private VBox menuPane;

  @FXML
  private VBox livresPane;

  @FXML
  private VBox membresPane;

  @FXML
  private VBox empruntsPane;

  @FXML
  public void initialize() {
    // Au démarrage, on affiche seulement le menu
    hideAll();
    menuPane.setVisible(true);
    System.out.println("MainController chargé avec succès");
  }

  private void hideAll() {
    menuPane.setVisible(false);
    livresPane.setVisible(false);
    membresPane.setVisible(false);
    empruntsPane.setVisible(false);
  }

  @FXML
  private void showLivres() {
    hideAll();
    livresPane.setVisible(true);
  }

  @FXML
  private void showMembres() {
    hideAll();
    membresPane.setVisible(true);
  }

  @FXML
  private void showEmprunts() {
    hideAll();
    empruntsPane.setVisible(true);
  }

  @FXML
  private void backToMenu() {
    hideAll();
    menuPane.setVisible(true);
  }
}
