package com.bibliotheque.service;

import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.model.Livre;
import com.bibliotheque.exception.ValidationException;
import java.util.List;

public class LivreService {
    private LivreDAO livreDAO;
    
    public LivreService(LivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }
    
    public void ajouterLivre(Livre livre) throws ValidationException {
        if (livre.getTitre() == null || livre.getTitre().trim().isEmpty()) {
            throw new ValidationException("Le titre est obligatoire");
        }
        if (livre.getAuteur() == null || livre.getAuteur().trim().isEmpty()) {
            throw new ValidationException("L'auteur est obligatoire");
        }
        if (livre.getIsbn() == null || livre.getIsbn().trim().isEmpty()) {
            throw new ValidationException("L'ISBN est obligatoire");
        }
        try {
            Livre existant = livreDAO.findById(livre.getIsbn());
            if (existant != null) {
                throw new ValidationException("Un livre avec cet ISBN existe déjà");
            }
            livreDAO.save(livre);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'ajout du livre: " + e.getMessage(), e);
        }
    }
    
    public void modifierLivre(Livre livre) throws ValidationException {
        try {
            livreDAO.update(livre);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la modification du livre: " + e.getMessage(), e);
        }
    }
    
    public void supprimerLivre(String isbn) {
        try {
            livreDAO.delete(isbn);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du livre: " + e.getMessage(), e);
        }
    }
    
    public List<Livre> rechercherLivres(String critere, String valeur) {
        try {
            switch (critere.toLowerCase()) {
                case "titre":
                    return livreDAO.findByTitre(valeur);
                case "auteur":
                    return livreDAO.findByAuteur(valeur);
                case "isbn":
                    Livre livre = livreDAO.findById(valeur);
                    return livre != null ? List.of(livre) : List.of();
                case "disponible":
                    return livreDAO.findDisponibles();
                case "tout":
                    return livreDAO.findByTitreOuAuteur(valeur);
                default:
                    return livreDAO.findAll();
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche: " + e.getMessage(), e);
        }
    }
    
    public List<Livre> getTousLivres() {
        try {
            return livreDAO.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du chargement des livres: " + e.getMessage(), e);
        }
    }
    
    public List<Livre> getLivresDisponibles() {
        try {
            return livreDAO.findDisponibles();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du chargement des livres disponibles: " + e.getMessage(), e);
        }
    }
    
    public Livre getLivreParIsbn(String isbn) {
        try {
            return livreDAO.findById(isbn);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche du livre: " + e.getMessage(), e);
        }
    }
}
