CREATE DATABASE covoiturage;
USE covoiturage;
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

