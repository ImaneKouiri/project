package com.bibliotheque.dao;

import com.bibliotheque.model.Livre;
import java.util.List;

public interface LivreDAO {
    void save(Livre livre);

    Livre findById(String id);

    List<Livre> findAll();

    void update(Livre livre);

    void delete(String id);

    List<Livre> findByTitre(String titre);

    List<Livre> findByAuteur(String auteur);

    Livre findByIsbn(String isbn);

    List<Livre> findDisponibles();
}
