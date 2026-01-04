package com.model;

public class Livre {
    public String Titre ;
    public String Isbn ;
    public boolean Disponible ;

    public String getTitre() {
        return Titre;
    }
    public void setTitre() {
        this.Titre = Titre;
    }
    public String getIsbn() {
        return Isbn;
    }
    public void setIsbn(String isbn) {
        this.Isbn = isbn;
    }
    public boolean isDisponible() {
        return Disponible;
    }   
    public void setDisponible(boolean disponible) {
        this.Disponible = disponible;
    }
    
}
