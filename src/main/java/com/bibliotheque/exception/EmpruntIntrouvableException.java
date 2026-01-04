package com.bibliotheque.exception;

public class EmpruntIntrouvableException extends Exception {
    
    public EmpruntIntrouvableException() {
        super("L'emprunt demandé n'existe pas.");
    }
    
    public EmpruntIntrouvableException(int empruntId) {
        super("Aucun emprunt trouvé avec l'ID : " + empruntId);
    }
}
