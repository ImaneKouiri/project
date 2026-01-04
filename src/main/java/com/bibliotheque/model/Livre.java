package com.bibliotheque.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.BooleanProperty;

public class Livre extends Document implements Empruntable {
    private String auteur;
    private int anneePublication;
    private boolean disponible;
    private String isbn;

    // Propriétés JavaFX pour TableView
    private final StringProperty isbnProperty;
    private final StringProperty titreProperty;
    private final StringProperty auteurProperty;
    private final IntegerProperty anneePublicationProperty;
    private final BooleanProperty disponibleProperty;

    public Livre(String isbn, String titre, String auteur, int anneePublication) {
        super(isbn, titre);
        this.isbn = isbn;
        this.auteur = auteur;
        this.anneePublication = anneePublication;
        this.disponible = true;
        this.isbnProperty = new SimpleStringProperty(isbn);
        this.titreProperty = new SimpleStringProperty(titre);
        this.auteurProperty = new SimpleStringProperty(auteur);
        this.anneePublicationProperty = new SimpleIntegerProperty(anneePublication);
        this.disponibleProperty = new SimpleBooleanProperty(true);
    }

    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) {
        this.auteur = auteur;
        this.auteurProperty.set(auteur);
    }

    public int getAnneePublication() { return anneePublication; }
    public void setAnneePublication(int anneePublication) {
        this.anneePublication = anneePublication;
        this.anneePublicationProperty.set(anneePublication);
    }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
        this.disponibleProperty.set(disponible);
    }

    public String getIsbn() { return isbn; }

    // Propriétés JavaFX
    public StringProperty isbnProperty() { return isbnProperty; }
    public StringProperty titreProperty() { return titreProperty; }
    public StringProperty auteurProperty() { return auteurProperty; }
    public IntegerProperty anneePublicationProperty() { return anneePublicationProperty; }
    public BooleanProperty disponibleProperty() { return disponibleProperty; }

    @Override
    public double calculerPenaliteRetard(int joursRetard) {
        return joursRetard * 0.50;
    }

    @Override
    public boolean peutEtreEmprunte() {
        return disponible;
    }

    @Override
    public void emprunter() {
        if (!disponible) {
            throw new IllegalStateException("Livre déjà emprunté");
        }
        this.disponible = false;
        this.disponibleProperty.set(false);
    }

    @Override
    public void retourner() {
        this.disponible = true;
        this.disponibleProperty.set(true);
    }

    @Override
    public String toString() {
        return titre + " (" + auteur + ", " + anneePublication + ")";
    }
}
