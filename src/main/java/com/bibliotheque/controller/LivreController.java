package fr.bibliotheque.controller;

import fr.bibliotheque.model.Livre;
import fr.bibliotheque.service.BibliothequeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

public class LivreController {
    
  
    
    // Tableau des livres
    @FXML
    private TableView<Livre> tableLivres;
    
    // Colonnes du tableau
    @FXML
    private TableColumn<Livre, String> colTitre;
    @FXML
    private TableColumn<Livre, String> colAuteur;
    @FXML
    private TableColumn<Livre, String> colIsbn;
    @FXML
    private TableColumn<Livre, String> colAnnee;
    @FXML
    private TableColumn<Livre, String> colStatut;
    
    // Champs du formulaire d'ajout
    @FXML
    private TextField txtTitre;
    @FXML
    private TextField txtAuteur;
    @FXML
    private TextField txtIsbn;
    @FXML
    private TextField txtAnnee;
    
    // Recherche
    @FXML
    private TextField txtRecherche;
    @FXML
    private ComboBox<String> comboRecherche;
    
    // Labels et boutons
    @FXML
    private Label lblStatus;
    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnRechercher;
    @FXML
    private Button btnActualiser;
    @FXML
    private Button btnSupprimer;
    
    //  VARIABLE DE CLASSE 
    
    private BibliothequeService service;
    private ObservableList<Livre> listeLivres;
    
    //  INITIALISATION 
    
    @FXML
    public void initialize() {
        System.out.println("Initialisation du contrôleur Livre...");
        
    
        service = new BibliothequeService();
      
        listeLivres = FXCollections.observableArrayList();
        
        configurerColonnes();
        
        configurerRecherche();
     
        chargerLivres();
        
      
        lblStatus.setText("Prêt - " + listeLivres.size() + " livre(s)");
        
        System.out.println("Contrôleur initialisé avec succès !");
    }
    
    //  CONFIGURATION 
    
    private void configurerColonnes() {
      
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colAuteur.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("anneePublication"));
    
        colStatut.setCellValueFactory(cellData -> {
            Livre livre = cellData.getValue();
            String statut = livre.peutEtreEmprunte() ? "✅ Disponible" : "❌ Emprunté";
            return new javafx.beans.property.SimpleStringProperty(statut);
        });
    }
    
    private void configurerRecherche() {
      
        comboRecherche.getItems().addAll("Tous", "Titre", "Auteur", "ISBN", "Disponibles");
        comboRecherche.setValue("Tous");
    }
    
    // MÉTHODES DE GESTION
    
    private void chargerLivres() {
        System.out.println("Chargement des livres...");
        try {
            listeLivres.clear();
            List<Livre> livres = service.getTousLesLivres();
            listeLivres.addAll(livres);
            tableLivres.setItems(listeLivres);
            
            lblStatus.setText("Chargé - " + livres.size() + " livre(s)");
            System.out.println("✅ " + livres.size() + " livre(s) chargé(s)");
            
        } catch (Exception e) {
            afficherErreur("Erreur", "Erreur lors du chargement : " + e.getMessage());
        }
    }
    
    //  GESTION DES ÉVÉNEMENTS 
    
    @FXML
    private void handleAjouter() {
        System.out.println("Ajout d'un nouveau livre...");
        
        try {
            // Récupérer les valeurs des champs
            String titre = txtTitre.getText().trim();
            String auteur = txtAuteur.getText().trim();
            String isbn = txtIsbn.getText().trim();
            String anneeTexte = txtAnnee.getText().trim();
            
            // Validation
            if (titre.isEmpty()) {
                afficherErreur("Erreur", "Le titre est obligatoire");
                return;
            }
            
            if (auteur.isEmpty()) {
                afficherErreur("Erreur", "L'auteur est obligatoire");
                return;
            }
            
            if (isbn.isEmpty()) {
                afficherErreur("Erreur", "L'ISBN est obligatoire");
                return;
            }
            
            int annee;
            try {
                annee = Integer.parseInt(anneeTexte);
            } catch (NumberFormatException e) {
                afficherErreur("Erreur", "L'année doit être un nombre");
                return;
            }
            
            // Générer un ID unique
            String id = "LIV" + System.currentTimeMillis();
            
            // Créer le livre
            Livre nouveauLivre = new Livre(id, titre, auteur, annee, isbn);
            
            // Ajouter via le service
            boolean succes = service.ajouterLivre(nouveauLivre);
            
            if (succes) {
                afficherMessage("Succès", "Livre ajouté avec succès !");
                viderChamps();
                chargerLivres(); // Recharger la liste
            } else {
                afficherErreur("Erreur", "Impossible d'ajouter le livre");
            }
            
        } catch (Exception e) {
            afficherErreur("Erreur", "Erreur lors de l'ajout : " + e.getMessage());
        }
    }
    
    @FXML
    private void handleSupprimer() {
        System.out.println("Suppression d'un livre...");
        
        // Récupérer le livre sélectionné
        Livre livreSelectionne = tableLivres.getSelectionModel().getSelectedItem();
        
        if (livreSelectionne == null) {
            afficherErreur("Erreur", "Veuillez sélectionner un livre à supprimer");
            return;
        }
        
        // Demander confirmation
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Supprimer le livre");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer : " + livreSelectionne.getTitre() + " ?");
        
        if (confirmation.showAndWait().get() == ButtonType.OK) {
            // Supprimer via le service
            boolean succes = service.supprimerLivre(livreSelectionne.getId());
            
            if (succes) {
                afficherMessage("Succès", "Livre supprimé avec succès");
                chargerLivres(); // Recharger la liste
            } else {
                afficherErreur("Erreur", "Impossible de supprimer le livre");
            }
        }
    }
    
    @FXML
    private void handleRechercher() {
        String critere = comboRecherche.getValue();
        String valeur = txtRecherche.getText().trim();
        
        System.out.println("Recherche : " + critere + " = " + valeur);
        
        if (critere.equals("Tous")) {
            chargerLivres(); // Recharger tout
            return;
        }
        
        try {
            listeLivres.clear();
            List<Livre> resultats = service.rechercherLivres(critere, valeur);
            listeLivres.addAll(resultats);
            
            lblStatus.setText("Recherche - " + resultats.size() + " résultat(s)");
            
            if (resultats.isEmpty()) {
                afficherMessage("Information", "Aucun livre trouvé");
            }
            
        } catch (Exception e) {
            afficherErreur("Erreur", "Erreur lors de la recherche : " + e.getMessage());
        }
    }
    
    @FXML
    private void handleActualiser() {
        System.out.println("Actualisation...");
        chargerLivres();
    }
    
    // MÉTHODES UTILITAIRES 
    
    private void viderChamps() {
        txtTitre.clear();
        txtAuteur.clear();
        txtIsbn.clear();
        txtAnnee.clear();
        txtTitre.requestFocus(); 
    }
    
    private void afficherMessage(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    //  MÉTHODES D'ACCÈS (pour tests)
    
    public BibliothequeService getService() {
        return service;
    }
    
    public ObservableList<Livre> getListeLivres() {
        return listeLivres;
    }
}
