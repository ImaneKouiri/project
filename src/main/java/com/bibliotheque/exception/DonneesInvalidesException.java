package com.bibliotheque.exception;

public class DonneesInvalidesException extends Exception {
    
    public DonneesInvalidesException(String message) {
        super(message);
    }
    
    //id membre invalide
    public static DonneesInvalidesException membreIdInvalide(String id) {
        return new DonneesInvalidesException(
            "L'ID membre '" + id + "' est invalide. Veuillez saisir un nombre entier positif."
        );
    }
    
    //id emprunt invalide
    public static DonneesInvalidesException empruntIdInvalide(String id) {
        return new DonneesInvalidesException(
            "L'ID emprunt '" + id + "' est invalide. Veuillez saisir un nombre entier positif."
        );
    }
    
    // isbn vide
    public static DonneesInvalidesException isbnVide() {
        return new DonneesInvalidesException(
            "L'ISBN ne peut pas être vide."
        );
    }
}
