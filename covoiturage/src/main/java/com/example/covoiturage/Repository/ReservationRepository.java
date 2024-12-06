package com.example.covoiturage.Repository;

import com.example.covoiturage.Entity.Reservation;
import com.example.covoiturage.Entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByPassager(Utilisateur passager); // Historique des réservations d'un utilisateur
}
