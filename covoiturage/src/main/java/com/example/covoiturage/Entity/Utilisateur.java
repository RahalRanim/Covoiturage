package com.example.covoiturage.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String mdp;
    private String nom;
    private String prenom;
    private String role;
    // Méthode pour vérifier si l'utilisateur est conducteur
    public boolean isEstConducteur() {
        return "conducteur".equalsIgnoreCase(this.role);
    }

    // Méthode pour vérifier si l'utilisateur est passager
    public boolean isEstPassager() {
        return "passager".equalsIgnoreCase(this.role);
    }



}
