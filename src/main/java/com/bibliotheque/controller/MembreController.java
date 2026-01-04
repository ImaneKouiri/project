package com.bibliotheque.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import com.bibliotheque.model.Membre;
import com.bibliotheque.service.BibliothequeService;
import com.bibliotheque.exception.ValidationException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javafx.scene.layout.GridPane;

public class MembreController {
    
    @FXML private TextField txtRecherche;
    @FXML private TableView<Membre> tableMembres;
    @FXML private TableColumn<Membre, Integer> colId;
    @FXML private TableColumn<Membre, String> colNom;
    @FXML private TableColumn<Membre, String> colPrenom;
    @FXML private TableColumn<Membre, String> colEmail;
    @FXML private TableColumn<Membre, String> colStatut;
    @FXML private TableColumn<Membre, Void> colActions;
    @FXML private Label lblTotal;
    @FXML private Label lblActifs;
    @FXML private Label lblInactifs;
    
    private BibliothequeService service;
    private ObservableList<Membre> membresData;
    
    @FXML
    public void initialize() {
        service = new BibliothequeService();
        membresData = FXCollections.observableArrayList();
        
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        
        configureColonneActions();
        tableMembres.setItems(membresData);
        chargerMembres();
    }
    
    private void configureColonneActions() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifier = new Button("✏ Modifier");
            private final Button btnToggleStatut = new Button();
            private final HBox hbox = new HBox(5, btnModifier, btnToggleStatut);
            
            {
                hbox.setAlignment(Pos.CENTER);
                btnModifier.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                
                btnModifier.setOnAction(event -> {
                    Membre membre = getTableView().getItems().get(getIndex());
                    handleModifierMembre(membre);
                });
                
                btnToggleStatut.setOnAction(event -> {
                    Membre membre = getTableView().getItems().get(getIndex());
                    handleToggleStatut(membre);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Membre membre = getTableView().getItems().get(getIndex());
                    btnToggleStatut.setText(membre.isActif() ? " Désactiver" : "Activer");
                    btnToggleStatut.setStyle(membre.isActif() ? 
                        "-fx-background-color: #f44336; -fx-text-fill: white;" :
                        "-fx-background-color: #4CAF50; -fx-text-fill: white;");
                    setGraphic(hbox);
                }
            }
        });
    }
    
    @FXML
    private void handleNouveauMembre() {
        Dialog<Membre> dialog = creerDialogMembre(null);
        Optional<Membre> result = dialog.showAndWait();
        
        result.ifPresent(membre -> {
            try {
                service.ajouterMembre(membre);
                afficherSucces("Succès", "Membre ajouté !");
                chargerMembres();
            } catch (ValidationException | SQLException e) {
                afficherErreur("Erreur", e.getMessage());
            }
        });
    }
    
    private void handleModifierMembre(Membre membre) {
        Dialog<Membre> dialog = creerDialogMembre(membre);
        Optional<Membre> result = dialog.showAndWait();
        
        result.ifPresent(membreModifie -> {
            try {
                service.modifierMembre(membreModifie);
                afficherSucces("Succès", "Membre modifié !");
                chargerMembres();
            } catch (ValidationException | SQLException e) {
                afficherErreur("Erreur", e.getMessage());
            }
        });
    }
    
    private void handleToggleStatut(Membre membre) {
        try {
            if (membre.isActif()) {
                service.desactiverMembre(membre.getId());
                afficherSucces("Succès", "Membre désactivé");
            } else {
                service.activerMembre(membre.getId());
                afficherSucces("Succès", "Membre activé");
            }
            chargerMembres();
        } catch (SQLException | ValidationException e) {
            afficherErreur("Erreur", e.getMessage());
        }
    }
    
    @FXML
    private void handleRecherche() {
        String critere = txtRecherche.getText();
        try {
            List<Membre> resultats = service.rechercherMembres(critere);
            membresData.setAll(resultats);
            mettreAJourStatistiques();
        } catch (SQLException e) {
            afficherErreur("Erreur de recherche", e.getMessage());
        }
    }
    
    private void chargerMembres() {
        try {
            List<Membre> membres = service.getTousMembres();
            membresData.setAll(membres);
            mettreAJourStatistiques();
        } catch (SQLException e) {
            afficherErreur("Erreur", e.getMessage());
        }
    }
    
    private void mettreAJourStatistiques() {
        long total = membresData.size();
        long actifs = membresData.stream().filter(Membre::isActif).count();
        long inactifs = total - actifs;
        
        lblTotal.setText("Total: " + total + " membres");
        lblActifs.setText("Actifs: " + actifs);
        lblInactifs.setText("Inactifs: " + inactifs);
    }
    
    private Dialog<Membre> creerDialogMembre(Membre membre) {
        Dialog<Membre> dialog = new Dialog<>();
        dialog.setTitle(membre == null ? "Nouveau Membre" : "Modifier Membre");
        
        ButtonType btnValider = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnValider, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
        
        TextField txtNom = new TextField(membre != null ? membre.getNom() : "");
        TextField txtPrenom = new TextField(membre != null ? membre.getPrenom() : "");
        TextField txtEmail = new TextField(membre != null ? membre.getEmail() : "");
        
        grid.add(new Label("Nom:"), 0, 0);
        grid.add(txtNom, 1, 0);
        grid.add(new Label("Prénom:"), 0, 1);
        grid.add(txtPrenom, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(txtEmail, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnValider) {
                Membre m = membre != null ? membre : new Membre();
                m.setNom(txtNom.getText());
                m.setPrenom(txtPrenom.getText());
                m.setEmail(txtEmail.getText());
                return m;
            }
            return null;
        });
        
        return dialog;
    }
    
    private void afficherSucces(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }
}