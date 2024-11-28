CREATE TABLE Utilisateur (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             email VARCHAR(255),
                             mdp VARCHAR(255),
                             nom VARCHAR(100),
                             prenom VARCHAR(100),
                             role VARCHAR(50)
);
INSERT INTO Utilisateur (email, mdp, nom, prenom, role)
VALUES ('user@example.com', 'password', 'Doe', 'John', 'conducteur'),
       ('passenger@example.com', 'password123', 'Smith', 'Jane', 'passager');
SELECT * FROM Utilisateur;
show tables;
CREATE TABLE Trajet (
                        idT BIGINT AUTO_INCREMENT PRIMARY KEY,
                        depart VARCHAR(255) NOT NULL,
                        destination VARCHAR(255) NOT NULL,
                        date DATE NOT NULL,
                        time TIME NOT NULL,
                        placeDispo INT NOT NULL,
                        prixPlace DOUBLE NOT NULL,
                        conducteur_id BIGINT,
                        CONSTRAINT fk_conducteur FOREIGN KEY (conducteur_id) REFERENCES Utilisateur(id) ON DELETE SET NULL
);
describe Trajet;
CREATE TABLE Reservation (
                             idR BIGINT AUTO_INCREMENT PRIMARY KEY,
                             passager_id BIGINT NOT NULL,
                             trajet_id BIGINT NOT NULL,
                             CONSTRAINT fk_passager FOREIGN KEY (passager_id) REFERENCES Utilisateur(id) ON DELETE CASCADE,
                             CONSTRAINT fk_trajet FOREIGN KEY (trajet_id) REFERENCES Trajet(idT) ON DELETE CASCADE
);
describe Reservation;

