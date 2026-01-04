package com.bibliotheque;

import com.bibliotheque.util.StringValidator;
import com.bibliotheque.util.DateUtils;
import com.bibliotheque.model.Membre;
import com.bibliotheque.model.Personne;

import java.time.LocalDate;

public class TestModel {

  public static void main(String[] args) {

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

    testMembre();
    System.out.println("===== FIN DES TESTS =====");

    
  }
  private static void testMembre() {
        
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
        
        System.out.println();
        System.out.println("===== FIN TEST MODULE MEMBRES =====");
    }
}
}
