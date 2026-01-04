package com.bibliotheque.dao;

import java.sql.SQLException;
import java.util.List;

import com.bibliotheque.model.Membre;

//Interface DAO pour la gestion des membres

public interface MembreDAO {

    void save(Membre membre) throws SQLException;
    Membre findById(int id) throws SQLException;
    List<Membre> findAll() throws SQLException;
    void update(Membre membre) throws SQLException;
    void delete(int id) throws SQLException;

    //LES METHODES DE GESTION 
    Membre findByEmail(String email) throws SQLException;
    List<Membre> findActifs() throws SQLException; 
}
