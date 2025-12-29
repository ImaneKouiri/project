CREATE DATABASE IF NOT EXISTS bibliotheque;
USE bibliotheque;

CREATE TABLE IF NOT EXISTS membres (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    actif BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS livres (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titre VARCHAR(200) NOT NULL,
    auteur VARCHAR(150),
    disponible BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS emprunts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    membre_id INT NOT NULL,
    livre_id INT NOT NULL,
    date_emprunt DATE NOT NULL,
    date_retour DATE,
    FOREIGN KEY (membre_id) REFERENCES membres(id),
    FOREIGN KEY (livre_id) REFERENCES livres(id)
);
