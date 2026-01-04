CREATE TABLE IF NOT EXISTS emprunts (
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
        REFERENCES membres(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE INDEX idx_emprunts_isbn
ON emprunts(isbn_livre);

CREATE INDEX idx_emprunts_membre
ON emprunts(id_membre);

CREATE INDEX idx_emprunts_rendu
ON emprunts(rendu);
-- emprunt non rendu
INSERT INTO emprunts (
    isbn_livre,
    id_membre,
    date_emprunt,
    date_retour_prevue
) VALUES (
    '978-0134685991',
    1,
    CURDATE(),
    DATE_ADD(CURDATE(), INTERVAL 14 DAY)
);
-- emprunt rendu à temps
INSERT INTO emprunts (
    isbn_livre,
    id_membre,
    date_emprunt,
    date_retour_prevue,
    date_retour_effective,
    rendu,
    penalite
) VALUES (
    '978-0201633610',
    2,
    '2025-01-01',
    '2025-01-15',
    '2025-01-14',
    TRUE,
    0.0
);
-- emprunt rendu en retard
INSERT INTO emprunts (
    isbn_livre,
    id_membre,
    date_emprunt,
    date_retour_prevue,
    date_retour_effective,
    rendu,
    penalite
) VALUES (
    '978-0321356680',
    3,
    '2024-12-01',
    '2024-12-15',
    '2024-12-20',
    TRUE,
    5.0
);
-- emprunt en cours
INSERT INTO emprunts (
    isbn_livre,
    id_membre,
    date_emprunt,
    date_retour_prevue
) VALUES (
    '978-0596009205',
    5,
    '2025-01-10',
    '2025-01-24'
);

