-- Créer la table membres
CREATE TABLE IF NOT EXISTS membres (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    actif BOOLEAN DEFAULT TRUE,
    date_inscription TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_actif (actif),
    INDEX idx_nom_prenom (nom, prenom)
);


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