package com.bibliotheque.model;

public class Membre extends Personne {
    private int id;
    private boolean actif;
    
    // Constructeur par défaut
    public Membre() {
        super();
        this.actif = true;
    }
    
    // Constructeur complet
    public Membre(int id, String nom, String prenom, String email, boolean actif) {
        super(nom, prenom, email);
        this.id = id;
        this.actif = actif;
    }
    
    // Constructeur sans ID
    public Membre(String nom, String prenom, String email) {
        super(nom, prenom, email);
        this.actif = true;
    }
    
    //  GETTERS 
    public int getId() {
        return id;
    }
    
    public boolean isActif() {
        return actif;
    }
    
    //  SETTERS 
    public void setId(int id) {
        this.id = id;
    }
    
    public void setActif(boolean actif) {
        this.actif = actif;
    }
    
    // Méthodes métier 
    public void activer() {
        this.actif = true;
    }
    
    public void desactiver() {
        this.actif = false;
    }
    
    public String getStatut() {
        return actif ? "Actif" : "Inactif";
    }
    
    @Override
    public String toString() {
        return "Membre{id=" + id + ", " + getNomComplet() + 
               ", email='" + getEmail() + "', actif=" + actif + "}";
    }
}