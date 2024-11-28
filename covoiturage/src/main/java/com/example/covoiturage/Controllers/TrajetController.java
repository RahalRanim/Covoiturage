package com.example.covoiturage.Controllers;


import com.example.covoiturage.Entity.Trajet;
import com.example.covoiturage.Entity.Utilisateur;
import com.example.covoiturage.Services.TrajetService;
import com.example.covoiturage.Services.UtilisateurService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class TrajetController {
    @Autowired
    private TrajetService trajetService;

    @Autowired
    private UtilisateurService utilisateurService;



    // Ajouter un trajet (accessible uniquement par un conducteur)
    @PostMapping("/ajouter")
    public ResponseEntity<?> ajouterTrajet(@RequestBody Trajet trajet, @RequestParam("email") String emailConducteur) {
        Utilisateur conducteur = utilisateurService.findByEmail(emailConducteur);

        // Vérifier si l'utilisateur est un conducteur
        if (conducteur != null && conducteur.isEstConducteur()) {
            // Vérifier si le trajet existe déjà
            if (trajetService.trajetExiste(trajet, conducteur)) {
                return ResponseEntity.badRequest().body("Un trajet similaire existe déjà.");
            }

            // Ajouter le trajet
            Trajet nouveauTrajet = trajetService.ajouterTrajet(trajet, conducteur);
            return ResponseEntity.ok(nouveauTrajet);
        } else {
            return ResponseEntity.badRequest().body("L'utilisateur doit être un conducteur pour ajouter un trajet.");
        }
    }


/*
    @GetMapping("/mes-trajets")
    public List<Trajet> afficherMesTrajets(HttpSession session) {
        Utilisateur currentUser = (Utilisateur) session.getAttribute("currentUser"); // Récupérer l'utilisateur connecté
        if (currentUser != null && currentUser.isEstConducteur()) {
            return trajetService.afficherTrajetsParConducteur(currentUser); // Afficher les trajets du conducteur
        } else {
            return null; // Aucun trajet ou utilisateur non autorisé
        }
    }*/


    @RequestMapping("/mes-trajets")
    public ResponseEntity<?> afficherTrajets(HttpSession session) {
        Utilisateur utilisateurConnecte = (Utilisateur) session.getAttribute("currentUser");

        if (utilisateurConnecte == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non connecté");
        }

        List<Trajet> trajets = trajetService.afficherTrajetsParConducteur(utilisateurConnecte);
        return ResponseEntity.ok(trajets);
    }


    @GetMapping("/rechercher-trajets")
    public ResponseEntity<?> rechercherTrajets(
            @RequestParam("email") String emailPassager, // Email du passager
            @RequestParam(required = false) String depart,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) LocalTime time,
            @RequestParam(required = false) Double prixMax,
            @RequestParam(required = false) String nomConducteur,
            @RequestParam(required = false) String prenomConducteur) {

        // Vérifiez si l'utilisateur est un passager
        Utilisateur passager = utilisateurService.findByEmail(emailPassager);
        if (passager == null || !passager.isEstPassager()) {
            return ResponseEntity.badRequest().body("L'utilisateur doit être un passager pour effectuer une recherche.");
        }

        // Appel du service pour effectuer la recherche
        List<Trajet> trajets = trajetService.rechercherTrajets(depart, destination, date, time, prixMax, nomConducteur, prenomConducteur);

        // Retourner les résultats
        return ResponseEntity.ok(trajets);
    }






}
