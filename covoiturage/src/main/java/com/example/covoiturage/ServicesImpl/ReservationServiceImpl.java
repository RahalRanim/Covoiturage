package com.example.covoiturage.ServicesImpl;

import com.example.covoiturage.Entity.Reservation;
import com.example.covoiturage.Entity.Trajet;
import com.example.covoiturage.Entity.Utilisateur;
import com.example.covoiturage.Repository.ReservationRepository;
import com.example.covoiturage.Repository.TrajetRepository;
import com.example.covoiturage.Services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TrajetRepository trajetRepository;

    @Override
    public Reservation reserverPlace(Utilisateur passager, Trajet trajet, int nombreDePlaces) {
        if (trajet.getPlaceDispo() < nombreDePlaces) {
            throw new IllegalArgumentException("Pas assez de places disponibles.");
        }

        trajet.setPlaceDispo(trajet.getPlaceDispo() - nombreDePlaces);
        trajetRepository.save(trajet);

        Reservation reservation = new Reservation();
        reservation.setPassager(passager);
        reservation.setTrajet(trajet);
        reservation.setNombreDePlaces(nombreDePlaces);
        return reservationRepository.save(reservation);
    }



    @Override
    public boolean annulerReservation(Long reservationId, Utilisateur passager) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Réservation introuvable."));

        // Vérifier si l'utilisateur connecté est bien le passager qui a fait la réservation
        if (reservation.getPassager().getId() != passager.getId()) {
            throw new SecurityException("Vous ne pouvez annuler que vos propres réservations.");
        }

        // Vérifier que la date actuelle ne dépasse pas la date du trajet
        Trajet trajet = reservation.getTrajet();
        if (trajet.getDate().isBefore(java.time.LocalDate.now())) { // Assurez-vous que 'getDate' retourne un LocalDate
            throw new IllegalStateException("Vous ne pouvez pas annuler une réservation après la date du trajet.");
        }

        // Ajouter les places réservées à la disponibilité du trajet
        int placesReservees = reservation.getNombreDePlaces();
        trajet.setPlaceDispo(trajet.getPlaceDispo() + placesReservees);
        trajetRepository.save(trajet);

        // Supprimer la réservation
        reservationRepository.delete(reservation);

        return true;
    }



    @Override
    public List<Reservation> getHistoriqueReservations(Utilisateur passager) {
        return reservationRepository.findByPassager(passager);
    }
}
