package fr.bibliotheque.service;

import fr.bibliotheque.dao.LivreDAO;
import fr.bibliotheque.dao.impl.LivreDAOImpl;
import fr.bibliotheque.model.Livre;
import java.util.List;

/**
 * Service pour la gestion des livres - Logique métier
 * 
 * Cette classe fait le lien entre le contrôleur et le DAO.
 * Elle contient la logique métier (validation, règles, etc.)
 */
public class BibliothequeService {
    
    private LivreDAO livreDAO;
    
    /**
     * Constructeur - Initialise le DAO
     */
    public BibliothequeService() {
        this.livreDAO = new LivreDAOImpl();
        System.out.println("✅ Service Bibliothèque initialisé");
    }
    
    //  MÉTHODES PRINCIPALES 
    
    /**
     * Ajouter un nouveau livre
     * @param livre Le livre à ajouter
     * @return true si succès, false sinon
     */
    public boolean ajouterLivre(Livre livre) {
        System.out.println("\n[Service] Tentative d'ajout d'un livre...");
        System.out.println("   Titre: " + livre.getTitre());
        System.out.println("   Auteur: " + livre.getAuteur());
        System.out.println("   ISBN: " + livre.getIsbn());
        
        try {
            // Validation de base
            if (livre.getTitre() == null || livre.getTitre().trim().isEmpty()) {
                System.out.println("❌ Échec : Le titre est obligatoire");
                return false;
            }
            
            if (livre.getAuteur() == null || livre.getAuteur().trim().isEmpty()) {
                System.out.println("❌ Échec : L'auteur est obligatoire");
                return false;
            }
            
            if (livre.getIsbn() == null || livre.getIsbn().trim().isEmpty()) {
                System.out.println("❌ Échec : L'ISBN est obligatoire");
                return false;
            }
            
            // Validation simplifiée de l'ISBN
            if (!validerISBN(livre.getIsbn())) {
                System.out.println("❌ Échec : ISBN invalide");
                return false;
            }
            
            // Vérifier si l'ISBN existe déjà (simulation)
            Livre existant = livreDAO.findByIsbn(livre.getIsbn());
            if (existant != null) {
                System.out.println("❌ Échec : Un livre avec cet ISBN existe déjà");
                return false;
            }
            
            // Générer un ID si nécessaire
            if (livre.getId() == null || livre.getId().isEmpty()) {
                livre.setId(genererIdLivre());
                System.out.println("   ID généré : " + livre.getId());
            }
            
            // Sauvegarder le livre
            livreDAO.save(livre);
            System.out.println("✅ Succès : Livre ajouté");
            return true;
            
        } catch (Exception e) {
            System.out.println("❌ Erreur : " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Modifier un livre existant
     * @param livre Le livre avec les modifications
     * @return true si succès, false sinon
     */
    public boolean modifierLivre(Livre livre) {
        System.out.println("\n[Service] Modification du livre ID: " + livre.getId());
        
        try {
            // Vérifier si le livre existe
            Livre existant = livreDAO.findById(livre.getId());
            if (existant == null) {
                System.out.println("❌ Échec : Livre non trouvé");
                return false;
            }
            
            // Validation
            if (livre.getTitre() == null || livre.getTitre().trim().isEmpty()) {
                System.out.println("❌ Échec : Le titre est obligatoire");
                return false;
            }
            
            // Mettre à jour
            livreDAO.update(livre);
            System.out.println("✅ Succès : Livre modifié");
            return true;
            
        } catch (Exception e) {
            System.out.println("❌ Erreur : " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Supprimer un livre par son ID
     * @param id L'ID du livre à supprimer
     * @return true si succès, false sinon
     */
    public boolean supprimerLivre(String id) {
        System.out.println("\n[Service] Suppression du livre ID: " + id);
        
        try {
            // Vérifier si le livre existe
            Livre livre = livreDAO.findById(id);
            if (livre == null) {
                System.out.println("❌ Échec : Livre non trouvé");
                return false;
            }
            
            // Vérifier si le livre est emprunté
            if (!livre.peutEtreEmprunte()) {
                System.out.println("❌ Échec : Le livre est actuellement emprunté");
                return false;
            }
            
            // Supprimer
            livreDAO.delete(id);
            System.out.println("✅ Succès : Livre supprimé");
            return true;
            
        } catch (Exception e) {
            System.out.println("❌ Erreur : " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Rechercher des livres par critère
     * @param critere Le critère de recherche (titre, auteur, isbn, tous, disponibles)
     * @param valeur La valeur à rechercher
     * @return Liste des livres correspondants
     */
    public List<Livre> rechercherLivres(String critere, String valeur) {
        System.out.println("\n[Service] Recherche : " + critere + " = " + valeur);
        
        try {
            switch (critere.toLowerCase()) {
                case "titre":
                    return livreDAO.findByTitre(valeur);
                    
                case "auteur":
                    return livreDAO.findByAuteur(valeur);
                    
                case "isbn":
                    Livre livre = livreDAO.findByIsbn(valeur);
                    return (livre != null) ? List.of(livre) : List.of();
                    
                case "tous":
                case "all":
                    return livreDAO.findAll();
                    
                case "disponibles":
                    return livreDAO.findDisponibles();
                    
                default:
                    System.out.println("⚠️  Critère inconnu, retour de tous les livres");
                    return livreDAO.findAll();
            }
        } catch (Exception e) {
            System.out.println("❌ Erreur de recherche : " + e.getMessage());
            return List.of();
        }
    }
    
    //  MÉTHODES DE RÉCUPÉRATION 
    
    /**
     * Obtenir tous les livres
     * @return Liste de tous les livres
     */
    public List<Livre> getTousLesLivres() {
        try {
            return livreDAO.findAll();
        } catch (Exception e) {
            System.out.println("❌ Erreur : " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Obtenir les livres disponibles
     * @return Liste des livres disponibles
     */
    public List<Livre> getLivresDisponibles() {
        try {
            return livreDAO.findDisponibles();
        } catch (Exception e) {
            System.out.println("❌ Erreur : " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Obtenir un livre par son ID
     * @param id L'ID du livre
     * @return Le livre trouvé ou null
     */
    public Livre getLivreParId(String id) {
        try {
            return livreDAO.findById(id);
        } catch (Exception e) {
            System.out.println("❌ Erreur : " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Obtenir un livre par son ISBN
     * @param isbn L'ISBN du livre
     * @return Le livre trouvé ou null
     */
    public Livre getLivreParIsbn(String isbn) {
        try {
            return livreDAO.findByIsbn(isbn);
        } catch (Exception e) {
            System.out.println("❌ Erreur : " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Obtenir les livres par titre
     * @param titre Le titre ou partie du titre
     * @return Liste des livres correspondants
     */
    public List<Livre> getLivresParTitre(String titre) {
        try {
            return livreDAO.findByTitre(titre);
        } catch (Exception e) {
            System.out.println("❌ Erreur : " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Obtenir les livres par auteur
     * @param auteur Le nom de l'auteur
     * @return Liste des livres de cet auteur
     */
    public List<Livre> getLivresParAuteur(String auteur) {
        try {
            return livreDAO.findByAuteur(auteur);
        } catch (Exception e) {
            System.out.println("❌ Erreur : " + e.getMessage());
            return List.of();
        }
    }
    
    //  MÉTHODES UTILITAIRES 
    
    /**
     * Valider un format ISBN (version simplifiée)
     * @param isbn L'ISBN à valider
     * @return true si valide, false sinon
     */
    private boolean validerISBN(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }
        
        // Format simplifié : doit contenir au moins 10 caractères
        String isbnNettoye = isbn.replaceAll("[^0-9X]", "");
        return isbnNettoye.length() >= 10;
    }
    
    /**
     * Générer un ID unique pour un livre
     * @return Un ID unique
     */
    public String genererIdLivre() {
        return "LIV" + System.currentTimeMillis();
    }
    
    /**
     * Compter le nombre total de livres
     * @return Le nombre de livres
     */
    public int compterLivres() {
        try {
            return livreDAO.findAll().size();
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Compter le nombre de livres disponibles
     * @return Le nombre de livres disponibles
     */
    public int compterLivresDisponibles() {
        try {
            return livreDAO.findDisponibles().size();
        } catch (Exception e) {
            return 0;
        }
    }
}