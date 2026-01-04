package com.bibliotheque.dao.impl;

import java.util.List;

import com.bibliotheque.dao.MembreDAO;
import com.bibliotheque.model.Membre;

import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;

public class MembreDAOImpl implements com.bibliotheque.dao.MembreDAO {
    // Implementation details here

    private final Connection connection =  DatabaseConnection.getInstance().getConnection();

    @Override
    public void save(Membre membre) throws SQLException {
        // Implementation here
        String sql = "INSERT INTO membres (nom, prenom, email, actif) VALUES (?, ?, ?, ?)";

        try (java.sql.PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, membre.getNom());
            pstmt.setString(2, membre.getPrenom());
            pstmt.setString(3, membre.getEmail());
            pstmt.setBoolean(4, membre.isActif());

            pstmt.executeUpdate();

            // Récupérer l'ID généré et le définir dans l'objet membre
            try (java.sql.ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    membre.setId(generatedKeys.getInt(1));
                }
            }
        }

    }
    @Override
    public Membre findById(int id) throws SQLException {
        // Implementation here
        String sql = "SELECT * FROM membres WHERE id = ?";

        try (java.sql.PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    com.bibliotheque.model.Membre membre = new com.bibliotheque.model.Membre();
                    membre.setId(rs.getInt("id"));
                    membre.setNom(rs.getString("nom"));
                    membre.setPrenom(rs.getString("prenom"));
                    membre.setEmail(rs.getString("email"));
                    membre.setActif(rs.getBoolean("actif"));
                    return membre;
                }
            }
        }
        return null;
    }
    @Override
    public java.util.List<com.bibliotheque.model.Membre> findAll() throws SQLException {
        // Implementation here
        java.util.List<com.bibliotheque.model.Membre> membres = new java.util.ArrayList<>();
        String sql = "SELECT * FROM membres";

        try (java.sql.Statement stmt = connection.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                com.bibliotheque.model.Membre membre = new com.bibliotheque.model.Membre();
                membre.setId(rs.getInt("id"));
                membre.setNom(rs.getString("nom"));
                membre.setPrenom(rs.getString("prenom"));
                membre.setEmail(rs.getString("email"));
                membre.setActif(rs.getBoolean("actif"));
                membres.add(membre);
            }
        }
        return membres;
    }
    @Override
    public void delete(int id) throws SQLException {
        // Implementation here
        String sql = "DELETE FROM membres WHERE id = ?";

        try (java.sql.PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    @Override
    public Membre findByEmail(String email) throws SQLException  {
        // Implementation here
        String sql = "SELECT * FROM membres WHERE email = ?";

        try (java.sql.PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);

            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    com.bibliotheque.model.Membre membre = new com.bibliotheque.model.Membre();
                    membre.setId(rs.getInt("id"));
                    membre.setNom(rs.getString("nom"));
                    membre.setPrenom(rs.getString("prenom"));
                    membre.setEmail(rs.getString("email"));
                    membre.setActif(rs.getBoolean("actif"));
                    return membre;
                }
            }
        }
        return null;
        
    }
    @Override
    public java.util.List<com.bibliotheque.model.Membre> findActifs() throws SQLException {
        // Implementation here
        java.util.List<com.bibliotheque.model.Membre> membresActifs = new java.util.ArrayList<>();
        String sql = "SELECT * FROM membres WHERE actif = TRUE";

        try (java.sql.Statement stmt = connection.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                com.bibliotheque.model.Membre membre = new com.bibliotheque.model.Membre();
                membre.setId(rs.getInt("id"));
                membre.setNom(rs.getString("nom"));
                membre.setPrenom(rs.getString("prenom"));
                membre.setEmail(rs.getString("email"));
                membre.setActif(rs.getBoolean("actif"));
                membresActifs.add(membre);
            }
        }
        return membresActifs;
    }

    @Override
    public void update (Membre membre) throws SQLException {
        String sql="UPDATE membres SET nom = ?, prenom = ?, email = ?, actif = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
         pstmt.setString(1, membre.getNom());
         pstmt.setString(2, membre.getPrenom());
         pstmt.setString(3, membre.getEmail());
         pstmt.setBoolean(4, membre.isActif());
         pstmt.setInt(5, membre.getId());
        
         pstmt.executeUpdate();
        }
    
    }
}

