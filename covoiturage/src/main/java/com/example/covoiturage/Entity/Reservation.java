package com.example.covoiturage.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idR;
    @ManyToOne
    @JoinColumn(name = "passager_id", nullable = false)
    private Utilisateur passager;
    @ManyToOne
    @JoinColumn(name = "trajet_id", nullable = false)
    private Trajet trajet;
    @Column(name = "nombre_places")
    private int nombreDePlaces;
}
