package com.example.covoiturage.Controllers;

import com.example.covoiturage.Entity.Reservation;
import com.example.covoiturage.Entity.Trajet;
import com.example.covoiturage.Entity.Utilisateur;
import com.example.covoiturage.Services.ReservationService;
import com.example.covoiturage.Services.TrajetService;
import com.example.covoiturage.Services.UtilisateurService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private TrajetService trajetService;

    @Autowired
    private UtilisateurService utilisateurService;

    // Réserver une place
    @PostMapping("/reserver")
    public ResponseEntity<?> reserverPlace(
            @RequestParam("IdT") Long trajetId,
            @RequestParam("PlaceDispo") int nombreDePlaces,
            @RequestParam("email") String email) {

        try {
            System.out.println("Réservation demandée pour le trajet ID: " + trajetId + ", places: " + nombreDePlaces + ", email: " + email);

            Utilisateur utilisateurConnecte = utilisateurService.findByEmail(email);
            if (utilisateurConnecte == null || !utilisateurConnecte.isEstPassager()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "success", false,
                        "message", "Seuls les passagers peuvent réserver."
                ));
            }

            Trajet trajet = trajetService.getTrajetById(trajetId);
            if (trajet.getPlaceDispo() < nombreDePlaces) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Pas assez de places disponibles."
                ));
            }

            Reservation reservation = reservationService.reserverPlace(utilisateurConnecte, trajet, nombreDePlaces);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Réservation réussie.",
                    "reservation", reservation
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Erreur interne: " + e.getMessage()
            ));
        }
    }


    @DeleteMapping("/annuler")
    public ResponseEntity<?> annulerReservation(
            @RequestParam("reservationId") Long reservationId,
            @RequestParam("email") String email) {

        System.out.println("Demande d'annulation reçue pour ID: " + reservationId + ", Email: " + email);

        Utilisateur utilisateurConnecte = utilisateurService.findByEmail(email);
        if (utilisateurConnecte == null || !utilisateurConnecte.isEstPassager()) {
            System.out.println("Utilisateur non autorisé ou non trouvé.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Seuls les passagers peuvent annuler leurs réservations.");
        }

        boolean success = reservationService.annulerReservation(reservationId, utilisateurConnecte);
        if (success) {
            System.out.println("Réservation annulée avec succès.");
            return ResponseEntity.ok("Réservation annulée avec succès.");
        } else {
            System.out.println("Erreur lors de l'annulation de la réservation.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de l'annulation.");
        }
    }



    // Historique des réservations
    @GetMapping("/historique")
    public ResponseEntity<?> getHistoriqueReservations(@RequestParam("email") String email) {
        Utilisateur utilisateurConnecte = utilisateurService.findByEmail(email);
        if (utilisateurConnecte == null || !utilisateurConnecte.isEstPassager()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non autorisé.");
        }

        List<Reservation> historique = reservationService.getHistoriqueReservations(utilisateurConnecte);
        return ResponseEntity.ok(historique);
    }
}
