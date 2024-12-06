package com.example.covoiturage.Services;
import com.example.covoiturage.Entity.Trajet;
import com.example.covoiturage.Entity.Utilisateur;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TrajetService {
    Trajet ajouterTrajet(Trajet trajet, Utilisateur conducteur);
    List<Trajet> afficherTrajetsParConducteur(Utilisateur conducteur);
    boolean trajetExiste(Trajet trajet, Utilisateur conducteur);
    List<Trajet> rechercherTrajets(String depart, String destination, LocalDate date, LocalTime time, Double prixMax, String nomConducteur, String prenomConducteur);
    Trajet getTrajetById(Long trajetId);
    List<Trajet> getTrajetsDisponiblesPourPassager();
    boolean isTrajetStillValid(Long trajetId);
}
