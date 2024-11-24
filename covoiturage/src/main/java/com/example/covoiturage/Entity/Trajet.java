package com.example.covoiturage.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Trajet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idT;
    private String depart;
    private String destination;
    private Date date;
    private Date time;
    private int placeDispo;
    private double prixPlace;
    @ManyToOne
    private Utilisateur conducteur;
}
