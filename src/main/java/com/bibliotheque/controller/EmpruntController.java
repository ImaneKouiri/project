package com.bibliotheque.controller;

import com.bibliotheque.model.Livre;
import com.bibliotheque.model.Membre;
import com.bibliotheque.exception.DonneesInvalidesException;
import com.bibliotheque.exception.EmpruntDejaRetourneException;
import com.bibliotheque.exception.EmpruntIntrouvableException;
import com.bibliotheque.exception.LimiteEmpruntDepasseeException;
import com.bibliotheque.service.BibliothequeService;
import com.bibliotheque.service.EmpruntService;
import com.bibliotheque.service.LivreService;
import com.bibliotheque.model.Emprunt;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

public class EmpruntController {

    @FXML
    private TextField isbnField;
    @FXML
    private TextField membreIdField;
    @FXML
    private TextField empruntIdField;
    @FXML
    private TextField rechercheMembreIdField;
    @FXML
    private TextArea resultatArea;
    @FXML
    private TableView<Emprunt> empruntsTable;
    @FXML
    private TableColumn<Emprunt, Integer> colId;
    @FXML
    private TableColumn<Emprunt, String> colIsbn;
    @FXML
    private TableColumn<Emprunt, Integer> colMembreId;
    @FXML
    private TableColumn<Emprunt, LocalDate> colDateEmprunt;
    @FXML
    private TableColumn<Emprunt, LocalDate> colDateRetourPrevue;
    @FXML
    private TableColumn<Emprunt, Boolean> colRendu;
    @FXML
    private TableColumn<Emprunt, Double> colPenalite;

    private final EmpruntService empruntService = new EmpruntService();
    private ObservableList<Emprunt> empruntsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (empruntsTable != null) {
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colIsbn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getLivre() != null ? cellData.getValue().getLivre().getIsbn() : "N/A"));
            colMembreId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(
                    cellData.getValue().getMembre() != null ? cellData.getValue().getMembre().getId() : 0).asObject());
            colDateEmprunt.setCellValueFactory(new PropertyValueFactory<>("dateEmprunt"));
            colDateRetourPrevue.setCellValueFactory(new PropertyValueFactory<>("dateRetourPrevue"));
            colRendu.setCellValueFactory(new PropertyValueFactory<>("rendu"));
            colPenalite.setCellValueFactory(new PropertyValueFactory<>("penalite"));

            empruntsTable.setItems(empruntsList);
        }
    }

    // Gestion de l'emprunt d'un livre
    @FXML
    private void handleEmprunter() {
        try {
            String isbn = isbnField.getText().trim();
            String membreIdText = membreIdField.getText().trim();

            if (isbn.isEmpty()) {
                throw DonneesInvalidesException.isbnVide();
            }

            if (membreIdText.isEmpty()) {
                showError("Validation", "Veuillez saisir l'ID du membre");
                return;
            }

            int membreId;
            try {
                membreId = Integer.parseInt(membreIdText);
                if (membreId <= 0) {
                    throw DonneesInvalidesException.membreIdInvalide(membreIdText);
                }
            } catch (NumberFormatException e) {
                throw DonneesInvalidesException.membreIdInvalide(membreIdText);
            }

            // Limite des emprunts
            if (!empruntService.peutEmprunter(membreId)) {
                int nbEmprunts = empruntService.countEmpruntsEnCours(membreId);
                showError("Limite atteinte",
                        "Ce membre a déjà " + nbEmprunts + " emprunts en cours.\n" +
                                "Il doit retourner un livre avant d'en emprunter un nouveau.\n\n" +
                                "Limite maximale : 3 emprunts simultanés");
                return;
            }
<<<<<<< HEAD

            // Livre livre = livreService.findByIsbn(isbn);
            Livre livre = new Livre();
=======
            
            Livre livre = new Livre(null, null, null, 0);
>>>>>>> bf43f1ae8d515c29ef20ba26de27d258b8113c7b
            livre.setIsbn(isbn);
            livre.setDisponible(true);

            Membre membre = new Membre();
            membre.setId(membreId);
<<<<<<< HEAD

=======
            
            
>>>>>>> bf43f1ae8d515c29ef20ba26de27d258b8113c7b
            empruntService.emprunterLivre(livre, membre);

            LocalDate dateRetour = LocalDate.now().plusWeeks(14);
            showInfo("Emprunt enregistré",
                    "Le livre a été emprunté avec succès !\n\n" +
                            "ISBN : " + isbn + "\n" +
                            "Membre : #" + membreId + "\n" +
                            "Date de retour prévue : " + dateRetour + "\n" +
                            "Durée : 14 semaines\n\n" +
                            "Pour éviter les pénalités (2 DH/jour), VEILLEZ RESPECTER LA DATE DE RETOUR.");

            clearEmpruntFields();
            rafraichirTableau();

        } catch (LimiteEmpruntDepasseeException e) {
            showError("Limite d'emprunts dépassée", e.getMessage());

        } catch (DonneesInvalidesException e) {
            showError("Données invalides", e.getMessage());

        } catch (Exception e) {
            showError("Erreur", "Une erreur inattendue s'est produite :\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    // Gestion du retour d'un livre

    @FXML
    private void handleRetour() {
        try {
            String empruntIdText = empruntIdField.getText().trim();

            if (empruntIdText.isEmpty()) {
                showError("Validation", "Veuillez saisir l'ID de l'emprunt à retourner");
                return;
            }

            int empruntId;
            try {
                empruntId = Integer.parseInt(empruntIdText);
                if (empruntId <= 0) {
                    throw DonneesInvalidesException.empruntIdInvalide(empruntIdText);
                }
            } catch (NumberFormatException e) {
                throw DonneesInvalidesException.empruntIdInvalide(empruntIdText);
            }

            // rechercher l'emprunt
            Emprunt emprunt = empruntService.findById(empruntId);
            // enregistrer l'emprunt
            int penalite = empruntService.retournerLivre(emprunt);
            // afficher les infos de retour
            if (penalite > 0) {
                long joursRetard = emprunt.calculerJoursRetard();
                showWarning("Retour effectué ",
                        "RETARD DÉTECTÉ\n" +
                                "Date retour prévue : " + emprunt.getDateRetourPrevue() + "\n" +
                                "Date retour effective : " + emprunt.getDateRetourEffective() + "\n" +
                                "Jours de retard : " + joursRetard + " jours\n\n" +
                                "Pénalité à payer : " + penalite + " DH\n" +
                                "   (Calcul : " + joursRetard + " jours × 2 DH/jour)");
            } else {
                showInfo("Retour effectué",
                        "Aucune pénalité\n" +
                                "Le retour a été effectué dans les délais.\n");
            }

            empruntIdField.clear();
            rafraichirTableau();

        } catch (EmpruntIntrouvableException e) {
            showError("Emprunt introuvable", e.getMessage());

        } catch (EmpruntDejaRetourneException e) {
            showError("Déjà retourné", e.getMessage());

        } catch (DonneesInvalidesException e) {
            showError("Données invalides", e.getMessage());

        } catch (Exception e) {
            showError("Erreur", "Une erreur inattendue s'est produite :\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    // Affichage des emprunts en cours
    @FXML
    private void handleAfficherEmpruntsEnCours() {
        try {
            List<Emprunt> emprunts = empruntService.getEmpruntsEnCours();

            if (emprunts.isEmpty()) {
                afficherResultat("Aucun emprunt en cours.\n");
                return;
            }

            StringBuilder sb = new StringBuilder();

            for (Emprunt e : emprunts) {
                sb.append(formatEmprunt(e)).append("\n");
            }

            afficherResultat(sb.toString());

            if (empruntsTable != null) {
                empruntsList.setAll(emprunts);
            }

        } catch (Exception e) {
            showError("Erreur", "Impossible de charger les emprunts en cours :\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    // Affichage des emprunts en retard
    @FXML
    private void handleAfficherEmpruntsEnRetard() {
        try {
            List<Emprunt> emprunts = empruntService.getEmpruntsEnRetard();

            if (emprunts.isEmpty()) {
                afficherResultat("Aucun emprunt en retard.\n");
                return;
            }

            StringBuilder sb = new StringBuilder();

            for (Emprunt e : emprunts) {
                sb.append(formatEmprunt(e));
                long joursRetard = e.calculerJoursRetard();
                double penaliteEstimee = joursRetard * 2.0;
                sb.append("Retard : ").append(joursRetard).append(" jours\n");
                sb.append("Pénalité estimée : ").append(penaliteEstimee).append(" DH\n\n");
            }

            afficherResultat(sb.toString());

            if (empruntsTable != null) {
                empruntsList.setAll(emprunts);
            }

        } catch (Exception e) {
            showError("Erreur", "Impossible de charger les emprunts en retard :\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    // Recherche des emprunts par membre
    @FXML
    private void handleRechercherParMembre() {
        try {
            String membreIdText = rechercheMembreIdField.getText().trim();

            if (membreIdText.isEmpty()) {
                showError("Validation", "Veuillez saisir l'ID du membre à rechercher");
                return;
            }

            int membreId;
            try {
                membreId = Integer.parseInt(membreIdText);
            } catch (NumberFormatException e) {
                throw DonneesInvalidesException.membreIdInvalide(membreIdText);
            }

            List<Emprunt> emprunts = empruntService.findByMembre(membreId);

            if (emprunts.isEmpty()) {
                afficherResultat("Aucun emprunt trouvé pour le membre #" + membreId + "\n\n" +
                        "Ce membre n'a jamais emprunté de livre.");
                return;
            }

            StringBuilder sb = new StringBuilder();

            int enCours = 0;
            double penalitesTotales = 0.0;

            for (Emprunt e : emprunts) {
                sb.append(formatEmprunt(e)).append("\n");
                if (!e.isRendu())
                    enCours++;
                penalitesTotales += e.getPenalite();
            }

            sb.append("Total emprunts : ").append(emprunts.size()).append("\n");
            sb.append("En cours : ").append(enCours).append("\n");
            sb.append("Terminés : ").append(emprunts.size() - enCours).append("\n");
            sb.append("Pénalités totales : ").append(String.format("%.2f", penalitesTotales)).append(" DH\n");

            afficherResultat(sb.toString());

            if (empruntsTable != null) {
                empruntsList.setAll(emprunts);
            }

        } catch (DonneesInvalidesException e) {
            showError("Données invalides", e.getMessage());
        } catch (Exception e) {
            showError("Erreur", "Impossible de rechercher les emprunts :\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    // Affichage des stat des emprunts
    @FXML
    private void handleAfficherStatistiques() {
        try {
            String stats = empruntService.getStatistiques();
            afficherResultat(stats);
        } catch (Exception e) {
            showError("Erreur", "Impossible de charger les statistiques :\n" + e.getMessage());
        }
    }

    // Methodes utiles

    // Formater un emprunt pour l'affichage
    private String formatEmprunt(Emprunt e) {
        StringBuilder sb = new StringBuilder();
        sb.append("Emprunt #").append(e.getId()).append("\n");
        sb.append("ISBN : ").append(e.getLivre() != null ? e.getLivre().getIsbn() : "N/A").append("\n");
        sb.append("Membre : #").append(e.getMembre() != null ? e.getMembre().getId() : "N/A").append("\n");
        sb.append("Date emprunt : ").append(e.getDateEmprunt()).append("\n");
        sb.append("Retour prévu : ").append(e.getDateRetourPrevue()).append("\n");
        sb.append("Statut : ").append(e.isRendu() ? "Rendu" : "En cours").append("\n");

        if (e.isRendu() && e.getDateRetourEffective() != null) {
            sb.append("Retour effectif : ").append(e.getDateRetourEffective()).append("\n");
        }

        if (e.getPenalite() > 0) {
            sb.append("Pénalité : ").append(e.getPenalite()).append(" DH\n");
        }

        return sb.toString();
    }

    // Refresh le tableau des emprunts
    private void rafraichirTableau() {
        if (empruntsTable != null) {
            List<Emprunt> emprunts = empruntService.getAllEmprunts();
            empruntsList.setAll(emprunts);
        }
    }

    // Affichage des results dans la zone de texte
    private void afficherResultat(String message) {
        if (resultatArea != null) {
            resultatArea.setText(message);
        } else {
            System.out.println(message);
        }
    }

    // Efface les champs du formulaire d'emprunt
    private void clearEmpruntFields() {
        isbnField.clear();
        membreIdField.clear();
    }

    // les alertes

    private void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
