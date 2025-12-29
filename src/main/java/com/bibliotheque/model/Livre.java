package fr.bibliotheque.model;

public class Livre extends Document implements Empruntable {
    private String auteur;
    private String isbn;
    private boolean disponible = true;
    
    public Livre(String id, String titre, String auteur, String isbn) {
        super(id, titre);
        this.auteur = auteur;
        this.isbn = isbn;
    }
    
    @Override
    public boolean peutEtreEmprunte() {
        return disponible;
    }
    
    // Getters
    public String getAuteur() { return auteur; }
    public String getIsbn() { return isbn; }
}