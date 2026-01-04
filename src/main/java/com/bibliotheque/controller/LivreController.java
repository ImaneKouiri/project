package com.bibliotheque.controller;

import com.bibliotheque.model.Livre;
import com.bibliotheque.service.BibliothequeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Contrôleur pour la vue de gestion des livres en JavaFX.
 * Gère les interactions utilisateur avec l'interface graphique.
 * 
 
 */
public class LivreController implements Initializable {
    
    // Composants de la table
    @FXML private TableView<Livre> tableLivres;
    @FXML private TableColumn<Livre, String> colId;
    @FXML private TableColumn<Livre, String> colTitre;
    @FXML private TableColumn<Livre, String> colAuteur;
    @FXML private TableColumn<Livre, Integer> colAnnee;
    @FXML private TableColumn<Livre, String> colIsbn;
    @FXML private TableColumn<Livre, Boolean> colDisponible;
    
    // Composants de recherche
    @FXML private TextField champRecherche;
    @FXML private ComboBox<String> comboCritere;
    
    // Composants d'action
    @FXML private Button boutonAjouter;
    @FXML private Button boutonModifier;
    @FXML private Button boutonSupprimer;
    @FXML private Button boutonRechercher;
    @FXML private Button boutonRafraichir;
    
    // Message utilisateur
    @FXML private Label labelMessage;
    
    // Services et données
    private BibliothequeService service;
    private ObservableList<Livre> livresObservable;
    
    /**
     * Initialise le contrôleur après le chargement du fichier FXML.
     * Configure les composants et charge les données initiales.
     * 
     * @param location URL utilisée pour résoudre les chemins relatifs
     * @param resources Ressources utilisées pour localiser les objets
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisation des services
        service = new BibliothequeService();
        livresObservable = FXCollections.observableArrayList();
        
        // Configuration des composants
        configurerTable();
        configurerRecherche();
        
        // Chargement des données
        chargerLivres();
    }
    
    /**
     * Configure les colonnes de la TableView.
     * Associe chaque colonne à une propriété de la classe Livre.
     */
    private void configurerTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colAuteur.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("anneePublication"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colDisponible.setCellValueFactory(new PropertyValueFactory<>("disponible"));
        
        tableLivres.setItems(livresObservable);
    }
    
    /**
     * Configure le composant de recherche.
     * Initialise la ComboBox avec les critères de recherche disponibles.
     */
    private void configurerRecherche() {
        comboCritere.getItems().addAll("Tous", "Titre", "Auteur", "ISBN");
        comboCritere.setValue("Tous");
    }
    
    /**
     * Charge tous les livres dans la TableView.
     */
    private void chargerLivres() {
        List<Livre> livres = service.rechercherLivres("tous", "");
        livresObservable.setAll(livres);
        afficherMessage("Chargement de " + livres.size() + " livre(s)");
    }
    
    /**
     * Gère l'action de recherche de livres.
     * Appelée lorsque l'utilisateur clique sur le bouton "Rechercher".
     */
    @FXML
    private void handleRechercher() {
        String critere = comboCritere.getValue().toLowerCase();
        String valeur = champRecherche.getText();
        
        List<Livre> resultats = service.rechercherLivres(critere, valeur);
        livresObservable.setAll(resultats);
        
        afficherMessage("Trouvé " + resultats.size() + " livre(s)");
    }
    
    /**
     * Gère l'action d'ajout d'un nouveau livre.
     * Crée un livre de test pour démonstration.
     */
    @FXML
    private void handleAjouter() {
        try {
            // Créer un livre de test
            String id = "L" + System.currentTimeMillis();
            Livre livre = new Livre(id, "Nouveau Livre", "Auteur Inconnu", 2024, "ISBN-" + id);
            
            service.ajouterLivre(livre);
            afficherMessage("Livre ajouté avec succès");
            chargerLivres();
        } catch (IllegalArgumentException e) {
            afficherMessage("Erreur: " + e.getMessage());
        }
    }
    
    /**
     * Gère l'action de modification d'un livre existant.
     * Modifie le titre du livre sélectionné.
     */
    @FXML
    private void handleModifier() {
        Livre selection = tableLivres.getSelectionModel().getSelectedItem();
        
        if (selection == null) {
            afficherMessage("Veuillez sélectionner un livre à modifier");
            return;
        }
        
        try {
            // Modifier le titre (exemple simple)
            selection.setTitre(selection.getTitre() + " (modifié)");
            service.modifierLivre(selection);
            
            afficherMessage("Livre modifié avec succès");
            tableLivres.refresh();
        } catch (IllegalArgumentException e) {
            afficherMessage("Erreur: " + e.getMessage());
        }
    }
    
    /**
     * Gère l'action de suppression d'un livre.
     * Demande confirmation avant de supprimer.
     */
    @FXML
    private void handleSupprimer() {
        Livre selection = tableLivres.getSelectionModel().getSelectedItem();
        
        if (selection == null) {
            afficherMessage("Veuillez sélectionner un livre à supprimer");
            return;
        }
        
        // Demander confirmation
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer le livre");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer \"" + selection.getTitre() + "\" ?");
        
        if (confirmation.showAndWait().get() == ButtonType.OK) {
            service.supprimerLivre(selection.getId());
            afficherMessage("Livre supprimé avec succès");
            chargerLivres();
        }
    }
    
    /**
     * Rafraîchit la liste des livres.
     * Recharge tous les livres depuis la base de données.
     */
    @FXML
    private void handleRafraichir() {
        chargerLivres();
        afficherMessage("Liste rafraîchie");
    }
    
    /**
     * Affiche un message à l'utilisateur.
     * 
     * @param message Message à afficher
     */
    private void afficherMessage(String message) {
        labelMessage.setText(message);
    }
    
    /**
     * Retourne le livre actuellement sélectionné dans la table.
     * 
     * @return Livre sélectionné, ou null si aucun livre n'est sélectionné
     */
    public Livre getLivreSelectionne() {
        return tableLivres.getSelectionModel().getSelectedItem();
    }
    
    /**
     * Vérifie si un livre est actuellement sélectionné.
     * 
     * @return true si un livre est sélectionné, false sinon
     */
    public boolean isLivreSelectionne() {
        return getLivreSelectionne() != null;
    }
}
