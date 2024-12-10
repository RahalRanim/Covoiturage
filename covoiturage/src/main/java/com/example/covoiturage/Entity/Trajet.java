package com.example.covoiturage.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "Trajet")
public class Trajet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idT;
    private String depart;
    private String destination;
    private LocalDate date;
    private LocalTime time;
    @Column(name = "place_dispo")
    private int placeDispo;
    private String com;
    @Column(name = "prix_place")
    private double prixPlace;
    @ManyToOne
    @JoinColumn(name = "conducteur_id")
    private Utilisateur conducteur;
}
