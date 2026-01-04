package com.bibliotheque.dao;

import com.bibliotheque.model.Emprunt;
import java.util.List;

// Gere l'acces aux donnees des emprunts
public interface EmpruntDAO {
    void save(Emprunt emprunt);
    Emprunt findByidEmprunt(int id);
    List<Emprunt> findByMembre(int membreId);
    List<Emprunt> findEnCours();
    int countEmpruntsEnCours(int membreId);
    void update(Emprunt emprunt);
    List<Emprunt> findAll();
    List<Emprunt> findEmpruntsEnRetard();
    boolean isLivreEmprunte(String isbn);
}

// controller -> service -> dao -> database