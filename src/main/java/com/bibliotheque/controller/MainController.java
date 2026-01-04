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

  // vues chargées une seule fois
  private Node livreView;
  private Node membreView;
  private Node empruntView;

  @FXML
  public void initialize() {
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

  private void loadLivreView() {
    if (livreView == null) {
      try {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/fxml/LivreView.fxml"));
        livreView = loader.load();
        livresPane.getChildren().add(livreView);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void loadMembreView() {
    if (membreView == null) {
      try {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/fxml/MembreView.fxml"));
        membreView = loader.load();
        membresPane.getChildren().add(membreView);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void loadEmpruntView() {
    if (empruntView == null) {
      try {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/fxml/EmpruntView.fxml"));
        empruntView = loader.load();
        empruntsPane.getChildren().add(empruntView);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @FXML
  private void showLivres() {
    hideAll();
    loadLivreView();
    livresPane.setVisible(true);
  }

  @FXML
  private void showMembres() {
    hideAll();
    loadMembreView();
    membresPane.setVisible(true);
  }

  @FXML
  private void showEmprunts() {
    hideAll();
    loadEmpruntView();
    empruntsPane.setVisible(true);
  }

  @FXML
  private void backToMenu() {
    hideAll();
    menuPane.setVisible(true);
  }
}
