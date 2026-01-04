package com.bibliotheque.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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

  private Node empruntView; // chargé une seule fois

  @FXML
  public void initialize() {
    hideAll();
    menuPane.setVisible(true);
    loadEmpruntView();
    System.out.println("MainController chargé avec succès");
  }

  private void hideAll() {
    menuPane.setVisible(false);
    livresPane.setVisible(false);
    membresPane.setVisible(false);
    empruntsPane.setVisible(false);
  }

  private void loadEmpruntView() {
    try {
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/fxml/EmpruntView.fxml"));
      empruntView = loader.load();
      empruntsPane.getChildren().add(empruntView);
    } catch (Exception e) {
      e.printStackTrace();
    }
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
