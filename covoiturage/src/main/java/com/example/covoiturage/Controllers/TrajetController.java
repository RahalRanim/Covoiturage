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

        if (conducteur == null || !conducteur.isEstConducteur()) {
            return ResponseEntity.badRequest().body("L'utilisateur doit être un conducteur pour ajouter un trajet.");
        }

        if (trajet.getDepart() == null || trajet.getDestination() == null || trajet.getDate() == null || trajet.getTime() == null) {
            return ResponseEntity.badRequest().body("Tous les champs doivent être remplis.");
        }

        if (trajetService.trajetExiste(trajet, conducteur)) {
            return ResponseEntity.badRequest().body("Un trajet similaire existe déjà.");
        }

        Trajet nouveauTrajet = trajetService.ajouterTrajet(trajet, conducteur);
        return ResponseEntity.ok(nouveauTrajet);
    }



    @RequestMapping("/mes-trajets")
    public ResponseEntity<?> afficherTrajets(@RequestParam("email") String emailConducteur) {
        // Vérifie si l'email est fourni
        if (emailConducteur == null || emailConducteur.isEmpty()) {
            return ResponseEntity.badRequest().body("L'email du conducteur est requis.");
        }

        // Recherche l'utilisateur par son email
        Utilisateur conducteur = utilisateurService.findByEmail(emailConducteur);

        // Vérifie si l'utilisateur existe et s'il est un conducteur
        if (conducteur == null || !conducteur.isEstConducteur()) {
            return ResponseEntity.badRequest().body("Aucun conducteur trouvé avec cet email ou l'utilisateur n'est pas un conducteur.");
        }

        // Récupère la liste des trajets du conducteur
        List<Trajet> trajets = trajetService.afficherTrajetsParConducteur(conducteur);

        // Si aucun trajet n'est trouvé, retourne un message approprié
        if (trajets.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun trajet trouvé pour ce conducteur.");
        }

        // Retourne la liste des trajets associés au conducteur
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

    @RequestMapping("/liste_trajetP")
    public ResponseEntity<?> afficherTrajetsDisponiblesPourPassager() {


        // Utilisation du nouveau nom de méthode
        List<Trajet> trajetsDisponibles = trajetService.getTrajetsDisponiblesPourPassager();
        return ResponseEntity.ok(trajetsDisponibles);
    }

    @GetMapping("/trajet-is-valid")
    public ResponseEntity<Boolean> isTrajetValid(@RequestParam("idT") Long id_trajet) {
        boolean isValid = trajetService.isTrajetStillValid(id_trajet);
        return ResponseEntity.ok(isValid);
    }







}
