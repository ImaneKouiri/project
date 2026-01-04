package fr.bibliotheque.model;

public abstract class Document {
    protected String id;
    protected String titre;
    
    public Document(String id, String titre) {
        this.id = id;
        this.titre = titre;
    }
    
    // Getters seulement pour commencer
    public String getId() { return id; }
    public String getTitre() { return titre; }

    