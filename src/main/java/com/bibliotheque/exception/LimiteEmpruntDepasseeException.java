package com.exception;

public class LimiteEmpruntDepasseeException extends Exception {
    public LimiteEmpruntDepasseeException() {
        super("Le membre a deja atteint le nombre maximal d'emprunt en cours (3 emprunt).");
    }
    
}
