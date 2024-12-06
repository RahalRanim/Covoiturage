package com.example.covoiturage.Services;
import com.example.covoiturage.Entity.Reservation;
import com.example.covoiturage.Entity.Trajet;
import com.example.covoiturage.Entity.Utilisateur;

import java.util.List;

public interface ReservationService {
    Reservation reserverPlace(Utilisateur passager, Trajet trajet, int nombreDePlaces);
    boolean annulerReservation(Long reservationId, Utilisateur passager);
    List<Reservation> getHistoriqueReservations(Utilisateur passager);
}
