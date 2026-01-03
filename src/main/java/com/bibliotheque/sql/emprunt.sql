CREATE TABLE emprunt (
    id INT AUTO_INCREMENT PRIMARY KEY,

    isbn_livre VARCHAR(20) NOT NULL,
    id_membre INT NOT NULL,

    date_emprunt DATE NOT NULL,
    date_retour_prevue DATE NOT NULL,
    date_retour_effective DATE,

    rendu BOOLEAN DEFAULT FALSE,

    CONSTRAINT fk_emprunt_livre
        FOREIGN KEY (isbn_livre)
        REFERENCES livre(isbn),

    CONSTRAINT fk_emprunt_membre
        FOREIGN KEY (id_membre)
        REFERENCES membre(id)
);