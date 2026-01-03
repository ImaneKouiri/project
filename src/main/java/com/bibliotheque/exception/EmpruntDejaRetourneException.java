package com.bibliotheque.exception;
import java.time.LocalDate;

public class EmpruntDejaRetourneException extends Exception {
    
    public EmpruntDejaRetourneException() {
        super("Ce livre a déjà été retourné. Impossible de traiter un nouveau retour.");
    }
    
    public EmpruntDejaRetourneException(int empruntId, LocalDate dateRetour) {
        super("L'emprunt #" + empruntId + " a déjà été retourné le " + dateRetour + ".");
    }
}

