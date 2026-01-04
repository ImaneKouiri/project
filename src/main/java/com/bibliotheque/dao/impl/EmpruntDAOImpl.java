package com.bibliotheque.dao.impl;

import com.bibliotheque.util.DatabaseConnection;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import com.bibliotheque.dao.EmpruntDAO;
import com.bibliotheque.model.Emprunt;
import com.bibliotheque.model.Livre;
import com.bibliotheque.model.Membre;

// La classe qui communique avec la base de donnees 
public class EmpruntDAOImpl implements EmpruntDAO {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public void save(Emprunt e) {
        String sql = "INSERT INTO emprunts(isbn_livre, id_membre, date_emprunt, date_retour_prevue, rendu, penalite) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, e.getLivre().getIsbn());
            ps.setInt(2, e.getMembre().getId());
            ps.setDate(3, Date.valueOf(e.getDateEmprunt()));
            ps.setDate(4, Date.valueOf(e.getDateRetourPrevue()));
            ps.setBoolean(5, false);
            ps.setDouble(6, 0.0);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la sauvegarde de l'emprunt : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public Emprunt findByidEmprunt(int id) {
        String sql = "SELECT * FROM emprunts WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToEmprunt(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'emprunt : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Emprunt> findByMembre(int membreId) {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunts WHERE id_membre=? ORDER BY date_emprunt DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, membreId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                emprunts.add(mapResultSetToEmprunt(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la recherche des emprunts du membre : " + ex.getMessage());
            ex.printStackTrace();
        }
        return emprunts;
    }

    @Override
    public List<Emprunt> findEnCours() {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunts WHERE rendu=false ORDER BY date_emprunt DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                emprunts.add(mapResultSetToEmprunt(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des emprunts en cours : " + e.getMessage());
            e.printStackTrace();
        }
        return emprunts;
    }

    @Override
    public int countEmpruntsEnCours(int membreId) {
        String sql = "SELECT COUNT(*) FROM emprunts WHERE id_membre=? AND rendu=false";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, membreId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des emprunts : " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void update(Emprunt e) {
        String sql = "UPDATE emprunts SET rendu=?, date_retour_effective=?, penalite=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBoolean(1, e.isRendu());

            // Gestion de la date de retour effective (peut être null)
            if (e.getDateRetourEffective() != null) {
                ps.setDate(2, Date.valueOf(e.getDateRetourEffective()));
            } else {
                ps.setNull(2, java.sql.Types.DATE);
            }

            ps.setDouble(3, e.getPenalite());
            ps.setInt(4, e.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                System.err.println("Aucun emprunt trouvé avec l'ID : " + e.getId());
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la mise à jour de l'emprunt : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public List<Emprunt> findAll() {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunts ORDER BY date_emprunt DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                emprunts.add(mapResultSetToEmprunt(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération de tous les emprunts : " + ex.getMessage());
            ex.printStackTrace();
        }
        return emprunts;
    }

    private Emprunt mapResultSetToEmprunt(ResultSet rs) throws SQLException {
        Emprunt emprunt = new Emprunt();

        emprunt.setId(rs.getInt("id"));
        emprunt.setDateEmprunt(rs.getDate("date_emprunt").toLocalDate());
        emprunt.setDateRetourPrevue(rs.getDate("date_retour_prevue").toLocalDate());

        Date dateRetourEffective = rs.getDate("date_retour_effective");
        if (dateRetourEffective != null) {
            emprunt.setDateRetourEffective(dateRetourEffective.toLocalDate());
        }

        emprunt.setRendu(rs.getBoolean("rendu"));
        emprunt.setPenalite(rs.getDouble("penalite"));

        Livre livre = new Livre(String isbn, String titre, String auteur, int anneePublication);
        livre.setIsbn(rs.getString("isbn_livre"));
        emprunt.setLivre(livre);

        Membre membre = new Membre();
        membre.setId(rs.getInt("id_membre"));
        emprunt.setMembre(membre);

        return emprunt;
    }

    public List<Emprunt> findEmpruntsEnRetard() {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunts WHERE rendu=false AND date_retour_prevue < CURDATE() ORDER BY date_retour_prevue ASC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                emprunts.add(mapResultSetToEmprunt(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des emprunts en retard : " + e.getMessage());
            e.printStackTrace();
        }
        return emprunts;
    }

    public boolean isLivreEmprunte(String isbn) {
        String sql = "SELECT COUNT(*) FROM emprunts WHERE isbn_livre=? AND rendu=false";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de disponibilité : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}