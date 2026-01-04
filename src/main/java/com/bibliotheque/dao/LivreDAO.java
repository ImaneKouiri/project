package com.bibliotheque.dao;

import com.bibliotheque.model.Livre;
import java.util.List;
import java.sql.SQLException;

public interface LivreDAO {
    void save(Livre livre) throws SQLException;
    Livre findById(String isbn) throws SQLException;
    List<Livre> findAll() throws SQLException;
    void update(Livre livre) throws SQLException;
    void delete(String isbn) throws SQLException;
    List<Livre> findByAuteur(String auteur) throws SQLException;
    List<Livre> findByTitre(String titre) throws SQLException;
    List<Livre> findDisponibles() throws SQLException;
    List<Livre> findByTitreOuAuteur(String recherche) throws SQLException;
}
