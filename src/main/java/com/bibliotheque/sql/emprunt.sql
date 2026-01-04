CREATE TABLE IF NOT EXISTS emprunt (
    id INT AUTO_INCREMENT PRIMARY KEY,

    isbn_livre VARCHAR(20) NOT NULL,
    id_membre INT NOT NULL,

    date_emprunt DATE NOT NULL,
    date_retour_prevue DATE NOT NULL,
    date_retour_effective DATE,

    rendu BOOLEAN DEFAULT FALSE,
    penalite DOUBLE DEFAULT 0.0,

    --index pour performance
    INDEX idx_emprunt_isbn (isbn_livre),
    INDEX idx_emprunt_membre (id_membre),
    INDEX idx_emprunt_rendu (rendu),

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

) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;

--emprunt non rendu
INSERT INTO emprunt (
    isbn_livre,
    id_membre,
    date_emprunt,
    date_retour_prevue
) VALUES (
    '978-2070612758',  
    1,                
    CURDATE(),
    DATE_ADD(CURDATE(), INTERVAL 14 DAY)
);

--emprunt rendu à temps
INSERT INTO emprunt (
    isbn_livre,
    id_membre,
    date_emprunt,
    date_retour_prevue,
    date_retour_effective,
    rendu,
    penalite
) VALUES (
    '978-2070368227',  
    2,                
    '2025-01-01',
    '2025-01-15',
    '2025-01-14',
    TRUE,
    0.0
);
--emprunt en retard
INSERT INTO emprunt (
    isbn_livre,
    id_membre,
    date_emprunt,
    date_retour_prevue,
    date_retour_effective,
    rendu,
    penalite
) VALUES (
    '978-2070360023',  
    3,                
    '2024-12-01',
    '2024-12-15',
    '2024-12-20',
    TRUE,
    5.0
);
--emprunt en cours
INSERT INTO emprunt (
    isbn_livre,
    id_membre,
    date_emprunt,
    date_retour_prevue
) VALUES (
    '978-2070612758',
    5,                
    '2025-01-10',
    '2025-01-24'
);
