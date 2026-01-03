package com.model;

import java.time.LocalDate;
import java.util.Objects;

public class Emprunt {

    private int id;
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective;
    private boolean rendu;
    private double penalite;
    private Livre livre;
    private Membre membre;

    public Emprunt(int id, LocalDate dateEmprunt, LocalDate dateRetourPrevue,LocalDate dateRetourEffective, boolean rendu, double penalite,Livre livre, Membre membre) {
        this.id = id;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetourEffective = dateRetourEffective;
        this.rendu = rendu;
        this.penalite = penalite;
        this.livre = livre;
        this.membre = membre;
    }

    // Constructeur pour un nouvel emprunt pas d'id
    public Emprunt( Livre livre, Membre membre,LocalDate dateEmprunt, LocalDate dateRetourPrevue) {
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.rendu = false;
        this.penalite = 0.0;
        this.livre = livre;
        this.membre = membre;
    }

    // Constructeurs par defaut
    public Emprunt() {
        this.rendu = false;
        this.penalite = 0.0;
    }

    // Getters & Setters

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }
    
    public void setDateEmprunt(LocalDate dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }
    
    public LocalDate getDateRetourPrevue() {
        return dateRetourPrevue;
    }
    
    public void setDateRetourPrevue(LocalDate dateRetourPrevue) {
        this.dateRetourPrevue = dateRetourPrevue;
    }
    
    public LocalDate getDateRetourEffective() {
        return dateRetourEffective;
    }
    
    public void setDateRetourEffective(LocalDate dateRetourEffective) {
        this.dateRetourEffective = dateRetourEffective;
    }
    
    public boolean isRendu() {
        return rendu;
    }
    
    public void setRendu(boolean rendu) {
        this.rendu = rendu;
    }
    
    public double getPenalite() {
        return penalite;
    }
    
    public void setPenalite(double penalite) {
        this.penalite = penalite;
    }
    
    public Livre getLivre() {
        return livre;
    }
    
    public void setLivre(Livre livre) {
        this.livre = livre;
    }
    
    public Membre getMembre() {
        return membre;
    }
    
    public void setMembre(Membre membre) {
        this.membre = membre;
    }

    // Methode pour verifier si un emprunt est en retard
    public boolean estEnRetard() {
        return !rendu && LocalDate.now().isAfter(dateRetourPrevue);
    }

    //Methode de calcul de nbr de jours de retard
    public long calculerJoursRetard() {
        if (!estEnRetard()) return 0 ;
        return java.time.temporal.ChronoUnit.DAYS.between(dateRetourPrevue, LocalDate.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Emprunt emprunt = (Emprunt) o;
        return id == emprunt.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Emprunt{" +
                "id=" + id +
                ", dateEmprunt=" + dateEmprunt +
                ", dateRetourPrevue=" + dateRetourPrevue +
                ", dateRetourEffective=" + dateRetourEffective +
                ", rendu=" + rendu +
                ", penalite=" + penalite +
                ", livre=" + (livre != null ? livre.getTitre() : "null") +
                ", membre=" + (membre != null ? membre.getNom() : "null") +
                '}';
    }
}
