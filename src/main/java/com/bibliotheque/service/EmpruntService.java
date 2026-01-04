package com.bibliotheque.service;

import com.bibliotheque.model.Membre;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.bibliotheque.dao.EmpruntDAO;
import com.bibliotheque.dao.impl.EmpruntDAOImpl;
import com.bibliotheque.exception.EmpruntDejaRetourneException;
import com.bibliotheque.exception.EmpruntIntrouvableException;
import com.bibliotheque.exception.LimiteEmpruntDepasseeException;
import com.bibliotheque.model.Emprunt;
import com.bibliotheque.model.Livre;

public class EmpruntService {
    private final EmpruntDAO empruntDAO = new EmpruntDAOImpl();
    
    private static final int MAX_EMPRUNTS_PAR_MEMBRE = 3;
    private static final int DUREE_EMPRUNT_SEMAINES = 14;
    private static final double PENALITE_PAR_JOUR = 2.0;

    public void emprunterLivre(Livre livre, Membre membre) throws LimiteEmpruntDepasseeException {
        // Vérification de la limite d'emprunts
        if (empruntDAO.countEmpruntsEnCours(membre.getId()) >= MAX_EMPRUNTS_PAR_MEMBRE) {
            throw new LimiteEmpruntDepasseeException();
        }

        // Création de l'emprunt avec date de retour prévue (14 semaines)
        LocalDate dateEmprunt = LocalDate.now();
        LocalDate dateRetourPrevue = dateEmprunt.plusWeeks(DUREE_EMPRUNT_SEMAINES);
        
        Emprunt emprunt = new Emprunt(livre, membre, dateEmprunt, dateRetourPrevue);
        empruntDAO.save(emprunt);
        
        System.out.println("Emprunt créé avec succès pour le membre " + membre.getId());
    }

    public int retournerLivre(Emprunt emprunt) throws EmpruntDejaRetourneException {
        if (emprunt.isRendu()) {
            throw new EmpruntDejaRetourneException();
        }
        emprunt.setRendu(true);
        emprunt.setDateRetourEffective(LocalDate.now());
        
        // Calcul de la pénalité
        int penalite = calculerPenalite(emprunt);
        
        // Mise à jour en base de données
        empruntDAO.update(emprunt);
        
        System.out.println("Retour enregistré. Pénalité : " + penalite + " DH");
        return penalite;
    }

    private int calculerPenalite(Emprunt emprunt) {
        LocalDate dateRetourEffective = emprunt.getDateRetourEffective();
        LocalDate dateRetourPrevue = emprunt.getDateRetourPrevue();

        if (dateRetourEffective.isAfter(dateRetourPrevue)) {
            long joursRetard = java.time.temporal.ChronoUnit.DAYS.between(
                dateRetourPrevue, 
                dateRetourEffective
            );
            double penalite = joursRetard * PENALITE_PAR_JOUR;
            emprunt.setPenalite(penalite);
            return (int) penalite;
        } else {
            emprunt.setPenalite(0.0);
            return 0;
        }
    }

    public Emprunt findById(int id) throws EmpruntIntrouvableException {  
        Emprunt emprunt = empruntDAO.findByidEmprunt(id);
        if (emprunt == null) {
            throw new EmpruntIntrouvableException(id);
        }
        return emprunt;
    }
    public List<Emprunt> findByMembre(int membreId) {
        return empruntDAO.findByMembre(membreId);
    }

    public List<Emprunt> getEmpruntsEnCours() {
        return empruntDAO.findEnCours();
    }

    public List<Emprunt> getEmpruntsEnRetard() {
        List<Emprunt> empruntsEnCours = empruntDAO.findEnCours();
        List<Emprunt> empruntsEnRetard = new ArrayList<>();
        
        for (Emprunt e : empruntsEnCours) {
            if (e.estEnRetard()) {
                empruntsEnRetard.add(e);
            }
        }
        return empruntsEnRetard;
    }

    public List<Emprunt> getAllEmprunts() {
        return empruntDAO.findAll();
    }

    public int countEmpruntsEnCours(int membreId) {
        return empruntDAO.countEmpruntsEnCours(membreId);
    }

    public boolean peutEmprunter(int membreId) {
        return empruntDAO.countEmpruntsEnCours(membreId) < MAX_EMPRUNTS_PAR_MEMBRE;
    }

    public boolean isLivreEmprunte(String isbn) {
        return empruntDAO.isLivreEmprunte(isbn);
    }

    public double calculerPenalitesTotales(int membreId) {
        List<Emprunt> emprunts = empruntDAO.findByMembre(membreId);
        double total = 0.0;
        
        for (Emprunt e : emprunts) {
            total += e.getPenalite();
        }
        
        return total;
    }


    public String getStatistiques() {
        List<Emprunt> tous = empruntDAO.findAll();
        List<Emprunt> enCours = empruntDAO.findEnCours();
        List<Emprunt> enRetard = getEmpruntsEnRetard();
        
        StringBuilder stats = new StringBuilder();
        stats.append("STATISTIQUES EMPRUNTS:\n");
        stats.append("Total emprunts : ").append(tous.size()).append("\n");
        stats.append("Emprunts en cours : ").append(enCours.size()).append("\n");
        stats.append("Emprunts en retard : ").append(enRetard.size()).append("\n");
        stats.append("Emprunts terminés : ").append(tous.size() - enCours.size()).append("\n");
        
        return stats.toString();
    }
}