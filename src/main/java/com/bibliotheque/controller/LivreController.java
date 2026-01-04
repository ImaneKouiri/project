package com.bibliotheque.controller;

import com.bibliotheque.model.Livre;
import com.bibliotheque.service.LivreService;
import com.bibliotheque.dao.impl.LivreDAOImpl;
import com.bibliotheque.exception.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import java.util.List;

public class LivreController {
    @FXML private TableView<Livre> tableLivres;
    @FXML private TableColumn<Livre, String> colIsbn;
    @FXML private TableColumn<Livre, String> colTitre;
    @FXML private TableColumn<Livre, String> colAuteur;
    @FXML private TableColumn<Livre, Integer> colAnnee;
    @FXML private TableColumn<Livre, String> colDisponible;

    @FXML private TextField txtRecherche;
    @FXML private ComboBox<String> comboCritere;

    @FXML private TextField txtIsbn;
    @FXML private TextField txtTitre;
    @FXML private TextField txtAuteur;
    @FXML private TextField txtAnnee;

    @FXML private Button btnAjouter;
    @FXML private Button btnModifier;
    @FXML private Button btnSupprimer;
    @FXML private Button btnRechercher;
    @FXML private Button btnEffacer;

    private LivreService livreService;
    private ObservableList<Livre> livresObservable;

    public void initialize() {
        livreService = new LivreService(new LivreDAOImpl());
        colIsbn.setCellValueFactory(cellData -> cellData.getValue().isbnProperty());
        colTitre.setCellValueFactory(cellData -> cellData.getValue().titreProperty());
        colAuteur.setCellValueFactory(cellData -> cellData.getValue().auteurProperty());
        colAnnee.setCellValueFactory(cellData -> cellData.getValue().anneePublicationProperty().asObject());
        colDisponible.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().isDisponible() ? "Oui" : "Non")
        );
        comboCritere.getItems().addAll("Titre", "Auteur", "ISBN", "Tout");
        comboCritere.setValue("Titre");
        livresObservable = FXCollections.observableArrayList();
        tableLivres.setItems(livresObservable);
        chargerTousLesLivres();
        tableLivres.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> afficherDetailsLivre(newSelection)
        );
    }

    private void chargerTousLesLivres() {
        try {
            List<Livre> livres = livreService.getTousLivres();
            livresObservable.setAll(livres);
        } catch (Exception e) {
            afficherErreur("Erreur", "Erreur lors du chargement des livres: " + e.getMessage());
        }
    }

    @FXML
    private void handleRechercher() {
        String critere = comboCritere.getValue();
        String valeur = txtRecherche.getText().trim();
        if (valeur.isEmpty() && !"Disponible".equalsIgnoreCase(critere)) {
            chargerTousLesLivres();
            return;
        }
        try {
            List<Livre> resultats = livreService.rechercherLivres(critere, valeur);
            livresObservable.setAll(resultats);
        } catch (Exception e) {
            afficherErreur("Erreur", "Erreur lors de la recherche: " + e.getMessage());
        }
    }

    @FXML
    private void handleAjouter() {
        try {
            if (txtIsbn.getText().isEmpty() || txtTitre.getText().isEmpty() ||
                txtAuteur.getText().isEmpty() || txtAnnee.getText().isEmpty()) {
                afficherErreur("Validation", "Tous les champs sont obligatoires");
                return;
            }
            Livre livre = new Livre(
                txtIsbn.getText(),
                txtTitre.getText(),
                txtAuteur.getText(),
                Integer.parseInt(txtAnnee.getText())
            );
            livreService.ajouterLivre(livre);
            chargerTousLesLivres();
            effacerFormulaire();
            afficherSucces("Succès", "Livre ajouté avec succès");
        } catch (ValidationException e) {
            afficherErreur("Validation", e.getMessage());
        } catch (NumberFormatException e) {
            afficherErreur("Validation", "L'année doit être un nombre");
        } catch (Exception e) {
            afficherErreur("Erreur", "Erreur lors de l'ajout: " + e.getMessage());
        }
    }

    @FXML
    private void handleModifier() {
        Livre livreSelectionne = tableLivres.getSelectionModel().getSelectedItem();
        if (livreSelectionne == null) {
            afficherErreur("Sélection", "Veuillez sélectionner un livre à modifier");
            return;
        }
        try {
            livreSelectionne.setTitre(txtTitre.getText());
            livreSelectionne.setAuteur(txtAuteur.getText());
            livreSelectionne.setAnneePublication(Integer.parseInt(txtAnnee.getText()));
            livreService.modifierLivre(livreSelectionne);
            tableLivres.refresh();
            afficherSucces("Succès", "Livre modifié avec succès");
        } catch (Exception e) {
            afficherErreur("Erreur", "Erreur lors de la modification: " + e.getMessage());
        }
    }

    @FXML
    private void handleSupprimer() {
        Livre livreSelectionne = tableLivres.getSelectionModel().getSelectedItem();
        if (livreSelectionne == null) {
            afficherErreur("Sélection", "Veuillez sélectionner un livre à supprimer");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le livre");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer le livre: " + livreSelectionne.getTitre() + "?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                livreService.supprimerLivre(livreSelectionne.getIsbn());
                livresObservable.remove(livreSelectionne);
                effacerFormulaire();
                afficherSucces("Succès", "Livre supprimé avec succès");
            } catch (Exception e) {
                afficherErreur("Erreur", "Erreur lors de la suppression: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleEffacer() {
        effacerFormulaire();
        tableLivres.getSelectionModel().clearSelection();
    }

    private void afficherDetailsLivre(Livre livre) {
        if (livre == null) {
            effacerFormulaire();
            return;
        }
        txtIsbn.setText(livre.getIsbn());
        txtTitre.setText(livre.getTitre());
        txtAuteur.setText(livre.getAuteur());
        txtAnnee.setText(String.valueOf(livre.getAnneePublication()));
    }

    private void effacerFormulaire() {
        txtIsbn.clear();
        txtTitre.clear();
        txtAuteur.clear();
        txtAnnee.clear();
    }

    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherSucces(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

