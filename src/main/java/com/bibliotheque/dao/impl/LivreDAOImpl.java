package com.bibliotheque.dao.impl;

import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.model.Livre;
import com.bibliotheque.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivreDAOImpl implements LivreDAO {
    private Connection connection;

    public LivreDAOImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Livre livre) throws SQLException {
        String sql = "INSERT INTO livres (isbn, titre, auteur, annee_publication, disponible) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, livre.getIsbn());
            stmt.setString(2, livre.getTitre());
            stmt.setString(3, livre.getAuteur());
            stmt.setInt(4, livre.getAnneePublication());
            stmt.setBoolean(5, livre.isDisponible());
            stmt.executeUpdate();
        }
    }

    @Override
    public Livre findById(String isbn) throws SQLException {
        String sql = "SELECT * FROM livres WHERE isbn = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLivre(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Livre> findAll() throws SQLException {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres ORDER BY titre";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                livres.add(mapResultSetToLivre(rs));
            }
        }
        return livres;
    }

    @Override
    public void update(Livre livre) throws SQLException {
        String sql = "UPDATE livres SET titre = ?, auteur = ?, annee_publication = ?, disponible = ? WHERE isbn = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, livre.getTitre());
            stmt.setString(2, livre.getAuteur());
            stmt.setInt(3, livre.getAnneePublication());
            stmt.setBoolean(4, livre.isDisponible());
            stmt.setString(5, livre.getIsbn());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String isbn) throws SQLException {
        String sql = "DELETE FROM livres WHERE isbn = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Livre> findByAuteur(String auteur) throws SQLException {
        return findByCritere("auteur", auteur);
    }

    @Override
    public List<Livre> findByTitre(String titre) throws SQLException {
        return findByCritere("titre", titre);
    }

    @Override
    public List<Livre> findDisponibles() throws SQLException {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE disponible = true ORDER BY titre";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                livres.add(mapResultSetToLivre(rs));
            }
        }
        return livres;
    }

    @Override
    public List<Livre> findByTitreOuAuteur(String recherche) throws SQLException {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE titre LIKE ? OR auteur LIKE ? ORDER BY titre";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String recherchePattern = "%" + recherche + "%";
            stmt.setString(1, recherchePattern);
            stmt.setString(2, recherchePattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    livres.add(mapResultSetToLivre(rs));
                }
            }
        }
        return livres;
    }

    private Livre mapResultSetToLivre(ResultSet rs) throws SQLException {
        Livre livre = new Livre(
            rs.getString("isbn"),
            rs.getString("titre"),
            rs.getString("auteur"),
            rs.getInt("annee_publication")
        );
        livre.setDisponible(rs.getBoolean("disponible"));
        return livre;
    }

    private List<Livre> findByCritere(String champ, String valeur) throws SQLException {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE " + champ + " LIKE ? ORDER BY titre";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + valeur + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    livres.add(mapResultSetToLivre(rs));
                }
            }
        }
        return livres;
    }
}


