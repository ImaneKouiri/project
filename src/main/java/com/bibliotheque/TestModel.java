package com.bibliotheque;

import com.bibliotheque.util.StringValidator;
import com.bibliotheque.util.DateUtils;
import com.bibliotheque.model.Membre;
import com.bibliotheque.model.Personne;
import com.bibliotheque.model.Emprunt;
import com.bibliotheque.model.Livre;
import com.bibliotheque.service.EmpruntService;
import com.bibliotheque.exception.LimiteEmpruntDepasseeException;
import com.bibliotheque.exception.EmpruntIntrouvableException;
import com.bibliotheque.exception.EmpruntDejaRetourneException;

import java.time.LocalDate;
import java.util.List;

public class TestModel {

    // ===== Variables globales pour les tests d'emprunts =====
    private static EmpruntService empruntService = new EmpruntService();
    private static int testsReussis = 0;
    private static int testsEchoues = 0;

    public static void main(String[] args) {

        System.out.println("===== TEST DES VALIDATIONS ET DES MODÈLES =====\n");

        // ===== Tests des validations simples =====
        testValidations();

        // ===== Tests du module Membre =====
        testMembre();

        // ===== Tests du module Emprunt =====
        testEmprunts();

        System.out.println("===== FIN DE TOUS LES TESTS =====");
    }

    // ====================== TESTS VALIDATIONS ======================
    private static void testValidations() {
        System.out.println("===== TEST DES VALIDATIONS =====");

        // Test Email
        System.out.println("Email valide (test@mail.com) : "
                + StringValidator.isValidEmail("test@mail.com"));

        System.out.println("Email invalide (test@mail) : "
                + StringValidator.isValidEmail("test@mail"));

        // Test ISBN
        System.out.println("ISBN valide (9780134685991) : "
                + StringValidator.isValidISBN("9780134685991"));

        System.out.println("ISBN invalide (1234) : "
                + StringValidator.isValidISBN("1234"));

        // Test Dates
        LocalDate today = LocalDate.now();
        LocalDate future = today.plusDays(7);

        System.out.println("Date future valide : "
                + DateUtils.isFutureDate(future));

        System.out.println("Période d'emprunt valide : "
                + DateUtils.isValidBorrowPeriod(today, future));

        System.out.println();
    }

    // ====================== TESTS MODULE MEMBRE ======================
    private static void testMembre() {

        System.out.println("===== TEST MODULE MEMBRES =====\n");

        // Test 1: Constructeurs
        System.out.println("--- Test 1: Constructeurs ---");
        Membre m1 = new Membre();
        System.out.println("Constructeur vide créé");

        Membre m2 = new Membre("Alaoui", "Fatima", "fatima@email.ma");
        System.out.println("Membre créé: " + m2.getNomComplet());

        Membre m3 = new Membre(1, "Bennis", "Youssef", "youssef@email.ma", true);
        System.out.println("Membre avec ID: " + m3.getNomComplet() + " (ID: " + m3.getId() + ")");
        System.out.println();

        // Test 2: Getters et Setters
        Membre membre = new Membre();
        membre.setId(10);
        membre.setNom("Benjelloun");
        membre.setPrenom("Sara");
        membre.setEmail("sara@email.ma");
        membre.setActif(true);

        System.out.println("ID: " + membre.getId());
        System.out.println("Nom complet: " + membre.getNomComplet());
        System.out.println("Email: " + membre.getEmail());
        System.out.println("Actif: " + membre.isActif());
        System.out.println();

        // Test 3: Activation/Désactivation
        Membre m4 = new Membre("Idrissi", "Karim", "karim@email.ma");
        System.out.println("État initial: " + m4.getStatut());

        m4.desactiver();
        System.out.println("Après désactivation: " + m4.getStatut());

        m4.activer();
        System.out.println("Après activation: " + m4.getStatut());
        System.out.println();

        // Test 4: Héritage
        System.out.println("--- Test 4: Héritage ---");
        Membre m5 = new Membre("Tazi", "Amina", "amina@email.ma");
        boolean estPersonne = m5 instanceof Personne;
        System.out.println("Membre est une instance de Personne: " + estPersonne);
        System.out.println();

        // Test 5: Plusieurs membres
        System.out.println("--- Test 5: Plusieurs membres ---");
        Membre[] membres = {
                new Membre(1, "Alami", "Hassan", "hassan@email.ma", true),
                new Membre(2, "Fassi", "Laila", "laila@email.ma", true),
                new Membre(3, "Mansouri", "Omar", "omar@email.ma", false),
                new Membre(4, "Chraibi", "Zineb", "zineb@email.ma", true)
        };

        System.out.println("Liste des membres:");
        for (Membre m : membres) {
            System.out.println("  [" + m.getId() + "] " + m.getNomComplet() + " - " + m.getStatut());
        }

        int actifs = 0;
        int inactifs = 0;
        for (Membre m : membres) {
            if (m.isActif()) {
                actifs++;
            } else {
                inactifs++;
            }
        }

        System.out.println("\nStatistiques:");
        System.out.println("Total: " + membres.length);
        System.out.println("Actifs: " + actifs);
        System.out.println("Inactifs: " + inactifs);

        System.out.println("\n===== FIN TEST MODULE MEMBRES =====\n");
    }

    private static void testEmprunts() {

        System.out.println("===== TEST MODULE EMPRUNTS =====\n");

        test1_CreerEmprunt();
        test2_VerifierLimite3Emprunts();
        test3_RetournerLivre();
        test4_CalculerPenalite();
        test5_RechercherEmprunts();
        test6_EmpruntsEnRetard();
        test7_StatistiquesGenerales();
        test8_ExceptionEmpruntIntrouvable();
        test9_ExceptionLivreDejaRetourne();

        afficherResultatsFinaux();
    }

    // ---------- TESTS EMPRUNTS ----------
    private static void test1_CreerEmprunt() {
        System.out.println("📌 TEST 1 : Création d'un emprunt");
        try {
            Livre livre = new Livre(null, null, null, 0);
            livre.setIsbn("978-TEST-0001");
            livre.setTitre("Livre de Test");
            livre.setDisponible(true);

            Membre membre = new Membre();
            membre.setId(999);
            membre.setNom("Membre Test");

            empruntService.emprunterLivre(livre, membre);

            int nbEmprunts = empruntService.countEmpruntsEnCours(999);

            if (nbEmprunts >= 1) testReussi("Emprunt créé avec succès");
            else testEchoue("L'emprunt n'a pas été enregistré");

        } catch (Exception e) {
            testEchoue("Erreur : " + e.getMessage());
        }
        System.out.println();
    }

    private static void test2_VerifierLimite3Emprunts() {
        System.out.println("TEST 2 : Vérification limite 3 emprunts");
        try {
            Membre membre = new Membre();
            membre.setId(998);
            membre.setNom("Membre Limite Test");

            for (int i = 1; i <= 3; i++) {
                Livre livre = new Livre(null, null, null, 0);
                livre.setIsbn("978-TEST-000" + i);
                livre.setDisponible(true);
                empruntService.emprunterLivre(livre, membre);
            }

            int count = empruntService.countEmpruntsEnCours(998);
            if (count == 3) System.out.println("✓ 3 emprunts créés correctement");

            try {
                Livre livre4 = new Livre(null, null, null, 0);
                livre4.setIsbn("978-TEST-0004");
                empruntService.emprunterLivre(livre4, membre);
                testEchoue("Le 4ème emprunt n'aurait PAS dû être autorisé !");
            } catch (LimiteEmpruntDepasseeException e) {
                testReussi("Limite de 3 emprunts respectée - Exception levée correctement");
            }

        } catch (Exception e) {
            testEchoue("Erreur : " + e.getMessage());
        }
        System.out.println();
    }

    private static void test3_RetournerLivre() {
        System.out.println("TEST 3 : Retour d'un livre");
        try {
            Livre livre = new Livre(null, null, null, 0);
            livre.setIsbn("978-TEST-RETOUR");
            livre.setDisponible(true);

            Membre membre = new Membre();
            membre.setId(997);

            empruntService.emprunterLivre(livre, membre);

            List<Emprunt> emprunts = empruntService.findByMembre(997);
            if (!emprunts.isEmpty()) {
                Emprunt emprunt = emprunts.get(0);
                int penalite = empruntService.retournerLivre(emprunt);

                if (penalite == 0) testReussi("Livre retourné sans pénalité");
                else testReussi("Livre retourné avec pénalité de " + penalite + " DH");
            } else testEchoue("Aucun emprunt trouvé pour effectuer le retour");

        } catch (Exception e) {
            testEchoue("Erreur : " + e.getMessage());
        }
        System.out.println();
    }

    private static void test4_CalculerPenalite() {
        System.out.println("TEST 4 : Calcul des pénalités");
        try {
            Livre livre = new Livre(null, null, null, 0);
            livre.setIsbn("978-TEST-PENALITE");

            Membre membre = new Membre();
            membre.setId(996);

            Emprunt emprunt = new Emprunt(
                    livre,
                    membre,
                    LocalDate.now().minusWeeks(20),
                    LocalDate.now().minusWeeks(6)
            );
            emprunt.setId(1);
            emprunt.setDateRetourEffective(LocalDate.now());

            long joursRetard = emprunt.calculerJoursRetard();
            System.out.println("  → Jours de retard : " + joursRetard);
            System.out.println("  → Pénalité estimée : " + (joursRetard * 2) + " DH");

            if (joursRetard > 0) testReussi("Calcul du retard fonctionne correctement");
            else testEchoue("Le calcul du retard ne fonctionne pas");

        } catch (Exception e) {
            testEchoue("Erreur : " + e.getMessage());
        }
        System.out.println();
    }

    private static void test5_RechercherEmprunts() {
        System.out.println("TEST 5 : Recherche d'emprunts");
        try {
            List<Emprunt> enCours = empruntService.getEmpruntsEnCours();
            System.out.println("  → Emprunts en cours : " + enCours.size());

            List<Emprunt> tous = empruntService.getAllEmprunts();
            System.out.println("  → Total emprunts : " + tous.size());

            if (tous.size() >= enCours.size()) testReussi("Recherche d'emprunts fonctionne");
            else testEchoue("Incohérence dans les résultats de recherche");

        } catch (Exception e) {
            testEchoue("Erreur : " + e.getMessage());
        }
        System.out.println();
    }

    private static void test6_EmpruntsEnRetard() {
        System.out.println("TEST 6 : Détection des emprunts en retard");
        try {
            List<Emprunt> enRetard = empruntService.getEmpruntsEnRetard();
            System.out.println("  → Emprunts en retard : " + enRetard.size());
            for (Emprunt e : enRetard) {
                System.out.println("    • Emprunt #" + e.getId() + " - Retard : " + e.calculerJoursRetard() + " jours");
            }
            testReussi("Détection des retards OK");
        } catch (Exception e) {
            testEchoue("Erreur : " + e.getMessage());
        }
        System.out.println();
    }

    private static void test7_StatistiquesGenerales() {
        System.out.println("TEST 7 : Génération des statistiques");
        try {
            String stats = empruntService.getStatistiques();
            System.out.println(stats);

            if (stats.contains("Total emprunts")) testReussi("Statistiques générées correctement");
            else testEchoue("Format des statistiques incorrect");

        } catch (Exception e) {
            testEchoue("Erreur : " + e.getMessage());
        }
        System.out.println();
    }

    private static void test8_ExceptionEmpruntIntrouvable() {
        System.out.println("TEST 8 : Exception EmpruntIntrouvable");
        try {
            empruntService.findById(99999);
            testEchoue("L'exception n'a pas été levée");
        } catch (EmpruntIntrouvableException e) {
            testReussi("Exception levée correctement : " + e.getMessage());
        } catch (Exception e) {
            testEchoue("Mauvaise exception : " + e.getMessage());
        }
        System.out.println();
    }

    private static void test9_ExceptionLivreDejaRetourne() {
        System.out.println("TEST 9 : Exception LivreDejaRetourne");
        try {
            Livre livre = new Livre(null, null, null, 0);
            livre.setIsbn("978-TEST-DOUBLE-RETOUR");

            Membre membre = new Membre();
            membre.setId(995);

            empruntService.emprunterLivre(livre, membre);

            List<Emprunt> emprunts = empruntService.findByMembre(995);
            if (!emprunts.isEmpty()) {
                Emprunt emprunt = emprunts.get(0);
                empruntService.retournerLivre(emprunt);

                try {
                    empruntService.retournerLivre(emprunt);
                    testEchoue("Le 2ème retour n'aurait PAS dû être autorisé");
                } catch (EmpruntDejaRetourneException e) {
                    testReussi("Exception levée correctement : " + e.getMessage());
                }
            }

        } catch (Exception e) {
            testEchoue("Erreur : " + e.getMessage());
        }
        System.out.println();
    }

    private static void testReussi(String message) {
        System.out.println("RÉUSSI : " + message);
        testsReussis++;
    }

    private static void testEchoue(String message) {
        System.out.println("ÉCHOUÉ : " + message);
        testsEchoues++;
    }

    private static void afficherResultatsFinaux() {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║              RÉSULTATS DES TESTS                     ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println("Tests réussis : " + testsReussis);
        System.out.println("Tests échoués : " + testsEchoues);
        System.out.println("Total : " + (testsReussis + testsEchoues));

        double pourcentage = (testsReussis * 100.0) / (testsReussis + testsEchoues);
        System.out.println("Taux de réussite : " + String.format("%.1f", pourcentage) + "%");

        if (testsEchoues == 0) System.out.println("\nTOUS LES TESTS SONT PASSÉS ! MODULE FONCTIONNEL !");
        else System.out.println("\nCertains tests ont échoué. Vérifiez les erreurs ci-dessus.");
        System.out.println("════════════════════════════════════════════════════════\n");
    }
}
