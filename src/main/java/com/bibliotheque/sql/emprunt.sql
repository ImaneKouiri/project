CREATE TABLE IF NOT EXISTS emprunt (
    id INT AUTO_INCREMENT PRIMARY KEY,

    isbn_livre VARCHAR(20) NOT NULL,
    id_membre INT NOT NULL,

    date_emprunt DATE NOT NULL,
    date_retour_prevue DATE NOT NULL,
    date_retour_effective DATE,

    rendu BOOLEAN DEFAULT FALSE,
    penalite DOUBLE DEFAULT 0.0,
    CONSTRAINT fk_emprunt_livre
        FOREIGN KEY (isbn_livre)
        REFERENCES livres(isbn)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_emprunt_membre
        FOREIGN KEY (id_membre)
        REFERENCES membre(id)
);
