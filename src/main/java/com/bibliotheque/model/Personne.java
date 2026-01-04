package com.bibliotheque.model;

public abstract class Personne {
    private String nom;
    private String prenom;
    private String email;
    
    // Constructeur par défaut
    public Personne() {
    }
    
    // Constructeur avec paramètres
    public Personne(String nom, String prenom, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }
    
    //  GETTERS 
    public String getNom() {
        return nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public String getEmail() {
        return email;
    }
    
    //  SETTERS
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    // Méthode utilitaire
    public String getNomComplet() {
        return prenom + " " + nom;
    }
    
    @Override
    public String toString() {
        return "Personne{nom='" + nom + "', prenom='" + prenom + "', email='" + email + "'}";
    }
}