package com.bibliotheque.dao.impl;

import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.model.Livre;
import java.util.ArrayList;
import java.util.List;

public class LivreDAOImpl implements LivreDAO {

    public LivreDAOImpl() {
        System.out.println("LivreDAOImpl créé (connexion BD à venir)");
    }

    @Override
    public void save(Livre livre) {
        System.out.println("[DAO] Sauvegarde du livre: " + livre.getTitre());
    }

    @Override
    public Livre findById(String id) {
        System.out.println("[DAO] Recherche par ID: " + id);
        return null;
    }

    @Override
    public List<Livre> findAll() {
        System.out.println("[DAO] Récupération de tous les livres");
        List<Livre> livres = new ArrayList<>();
        livres.add(new Livre("LIV001", "Le Petit Prince", "Saint-Exupéry", 1943, "978-xxx"));
        livres.add(new Livre("LIV002", "1984", "George Orwell", 1949, "978-yyy"));
        return livres;
    }

    @Override
    public void update(Livre livre) {
        System.out.println("[DAO] Mise à jour du livre: " + livre.getId());
    }

    @Override
    public void delete(String id) {
        System.out.println("[DAO] Suppression du livre ID: " + id);
    }

    @Override
    public List<Livre> findByTitre(String titre) {
        System.out.println("[DAO] Recherche par titre: " + titre);
        return new ArrayList<>();
    }

    @Override
    public List<Livre> findByAuteur(String auteur) {
        System.out.println("[DAO] Recherche par auteur: " + auteur);
        return new ArrayList<>();
    }

    @Override
    public Livre findByIsbn(String isbn) {
        System.out.println("[DAO] Recherche par ISBN: " + isbn);
        return null;
    }

    @Override
    public List<Livre> findDisponibles() {
        System.out.println("[DAO] Recherche des livres disponibles");
        return new ArrayList<>();
    }
}
