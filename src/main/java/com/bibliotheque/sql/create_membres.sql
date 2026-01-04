-- ============================================
-- Script de création de la table MEMBRES
-- Étudiant B - Module Gestion des Adhérents
-- ============================================

-- Créer la base de données (si elle n'existe pas)
CREATE DATABASE IF NOT EXISTS bibliotheque;
USE bibliotheque;

-- Supprimer la table si elle existe déjà (pour réinitialiser)
DROP TABLE IF EXISTS membres;

-- Créer la table membres
CREATE TABLE membres (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    actif BOOLEAN DEFAULT TRUE,
    date_inscription TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_actif (actif),
    INDEX idx_nom_prenom (nom, prenom)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Insertion de données de test
-- ============================================

INSERT INTO membres (nom, prenom, email, actif) VALUES
('Dupont', 'Jean', 'jean.dupont@email.com', TRUE),
('Martin', 'Marie', 'marie.martin@email.com', TRUE),
('Bernard', 'Pierre', 'pierre.bernard@email.com', TRUE),
('Dubois', 'Sophie', 'sophie.dubois@email.com', FALSE),
('Petit', 'Lucas', 'lucas.petit@email.com', TRUE),
('Robert', 'Emma', 'emma.robert@email.com', TRUE),
('Richard', 'Thomas', 'thomas.richard@email.com', FALSE),
('Moreau', 'Léa', 'lea.moreau@email.com', TRUE),
('Simon', 'Camille', 'camille.simon@email.com', TRUE),
('Laurent', 'Hugo', 'hugo.laurent@email.com', TRUE),
('Lefebvre', 'Chloé', 'chloe.lefebvre@email.com', FALSE),
('Michel', 'Nathan', 'nathan.michel@email.com', TRUE),
('Garcia', 'Manon', 'manon.garcia@email.com', TRUE),
('David', 'Louis', 'louis.david@email.com', TRUE),
('Bertrand', 'Sarah', 'sarah.bertrand@email.com', TRUE);

-- ============================================
-- Vérification
-- ============================================

-- Afficher tous les membres
SELECT * FROM membres;

-- Statistiques
SELECT 
    COUNT(*) AS total_membres,
    SUM(CASE WHEN actif = TRUE THEN 1 ELSE 0 END) AS membres_actifs,
    SUM(CASE WHEN actif = FALSE THEN 1 ELSE 0 END) AS membres_inactifs
FROM membres;

-- Membres actifs uniquement
SELECT id, nom, prenom, email 
FROM membres 
WHERE actif = TRUE 
ORDER BY nom, prenom;