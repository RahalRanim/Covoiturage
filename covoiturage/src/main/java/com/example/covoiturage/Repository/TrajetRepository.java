package com.example.covoiturage.Repository;

import com.example.covoiturage.Entity.Trajet;
import com.example.covoiturage.Entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TrajetRepository extends JpaRepository<Trajet, Long> {
    // Recherche des trajets par conducteur
    List<Trajet> findByConducteur(Utilisateur conducteur);

    // Recherche d'un trajet similaire pour un conducteur donné
    @Query("SELECT t FROM Trajet t WHERE t.conducteur = :conducteur AND t.date = :date AND t.time = :time")
    List<Trajet> veriftrajet(Utilisateur conducteur, LocalDate date, LocalTime time);


    @Query("SELECT t FROM Trajet t " +
            "WHERE (:depart IS NULL OR t.depart = :depart) " +
            "AND (:destination IS NULL OR t.destination = :destination) " +
            "AND (:date IS NULL OR t.date = :date) " +
            "AND (:time IS NULL OR t.time = :time) " +
            "AND (:prixMax IS NULL OR t.prixPlace <= :prixMax) " +
            "AND (:nomConducteur IS NULL OR t.conducteur.nom = :nomConducteur) " +
            "AND (:prenomConducteur IS NULL OR t.conducteur.prenom = :prenomConducteur)")
    List<Trajet> rechercheAvanceeTrajets(
            @Param("depart") String depart,
            @Param("destination") String destination,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time,
            @Param("prixMax") Double prixMax,
            @Param("nomConducteur") String nomConducteur,
            @Param("prenomConducteur") String prenomConducteur);

    List<Trajet> findByPlaceDispoGreaterThan(int placeDispo);
}
