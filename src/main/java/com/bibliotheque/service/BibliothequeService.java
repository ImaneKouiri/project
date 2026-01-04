package com.bibliotheque.service;

import com.bibliotheque.dao.MembreDAO;
import com.bibliotheque.dao.impl.MembreDAOImpl;
import com.bibliotheque.model.Membre;
import com.bibliotheque.dao.EmpruntDAO;  
import com.bibliotheque.dao.impl.EmpruntDAOImpl; 
import com.bibliotheque.model.Emprunt;
import com.bibliotheque.exception.ValidationException;
import com.bibliotheque.exception.MembreInactifException;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class BibliothequeService {
    private MembreDAO membreDAO;
    private EmpruntDAO empruntDAO; 

    public BibliothequeService() {
        this.membreDAO = new MembreDAOImpl();
        this.empruntDAO = new EmpruntDAOImpl(); 
    }

    // Ajoute un nouveau membre
    public void ajouterMembre(Membre membre) throws ValidationException, SQLException { // ← Ajouté SQLException
        validateMembre(membre);

        // Vérifier si l'email existe déjà
        Membre membreExistant = membreDAO.findByEmail(membre.getEmail());
        if (membreExistant != null) {
            throw new ValidationException("Un membre avec cet email existe déjà.");
        }

        membreDAO.save(membre);
    }

    // Modifie un membre existant
    public void modifierMembre(Membre membre) throws ValidationException, SQLException {
        // Validation
        validateMembre(membre);

        // Vérifier si le membre existe
        Membre membreExistant = membreDAO.findById(membre.getId());
        if (membreExistant == null) {
            throw new ValidationException("Le membre avec l'ID spécifié n'existe pas.");
        }

        // Vérifier si l'email est utilisé par un autre membre
        Membre autreEmail = membreDAO.findByEmail(membre.getEmail());
        if (autreEmail != null && autreEmail.getId() != membre.getId()) {
            throw new ValidationException("Un autre membre utilise cet email.");
        }

        // Mise à jour
        membreDAO.update(membre);
    }

    // Active un membre

    public void activerMembre(int id) throws SQLException, ValidationException {
        Membre membre = membreDAO.findById(id);
        if (membre == null) {
            throw new ValidationException("Le membre avec l'ID spécifié n'existe pas.");
        }

        if (membre.isActif()) {
            throw new ValidationException("Le membre est déjà actif.");
        }

        membre.setActif(true);
        membreDAO.update(membre);
    }

    // Désactive un membre

    public void desactiverMembre(int id) throws SQLException, ValidationException {
        Membre membre = membreDAO.findById(id);
        if (membre == null) {
            throw new ValidationException("Le membre avec l'ID spécifié n'existe pas.");
        }

        if (!membre.isActif()) {
            throw new ValidationException("Le membre est déjà inactif.");
        }

        membre.setActif(false);
        membreDAO.update(membre);
    }

    // Recherche des membres par critère

    public List<Membre> rechercherMembres(String critere) throws SQLException {
        List<Membre> tousLesMembres = membreDAO.findAll();

        if (critere == null || critere.trim().isEmpty()) {
            return tousLesMembres;
        }

        String recherche = critere.toLowerCase();
        return tousLesMembres.stream()
                .filter(membre -> membre.getNom().toLowerCase().contains(recherche) ||
                        membre.getPrenom().toLowerCase().contains(recherche) ||
                        membre.getEmail().toLowerCase().contains(recherche))
                .collect(Collectors.toList());
    }

    // Récupère tous les membres actifs

    public List<Membre> getMembresActifs() throws SQLException {
        return membreDAO.findActifs();
    }

    // Récupère tous les membres

    public List<Membre> getTousMembres() throws SQLException {
        return membreDAO.findAll();
    }

    // Valide les données d'un membre

    private void validateMembre(Membre membre) throws ValidationException {
        if (membre == null) {
            throw new ValidationException("Le membre ne peut pas être null.");
        }

        if (membre.getNom() == null || membre.getNom().trim().isEmpty()) {
            throw new ValidationException("Le nom du membre est obligatoire.");
        }

        if (membre.getPrenom() == null || membre.getPrenom().trim().isEmpty()) {
            throw new ValidationException("Le prénom du membre est obligatoire.");
        }

        if (membre.getEmail() == null || membre.getEmail().trim().isEmpty()) {
            throw new ValidationException("L'email du membre est obligatoire.");
        }

        // Validation simple de l'email
        if (!membre.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("L'email du membre n'est pas valide.");
        }
    }

    public List<Emprunt> getHistoriqueEmprunts(int membreId) throws SQLException, ValidationException {
    // Vérifier que le membre existe
    Membre membre = membreDAO.findById(membreId);
    if (membre == null) {
        throw new ValidationException("Membre introuvable avec l'ID : " + membreId);
    }
    
    // Récupérer tous les emprunts du membre via le DAO
    return empruntDAO.findByMembre(membreId);
 }

}
