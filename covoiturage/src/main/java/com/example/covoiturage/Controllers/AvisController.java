package com.example.covoiturage.Controllers;

import com.example.covoiturage.Entity.Avis;
import com.example.covoiturage.Entity.AvisRequest;
import com.example.covoiturage.Entity.Trajet;
import com.example.covoiturage.Entity.Utilisateur;
import com.example.covoiturage.Services.AvisService;
import com.example.covoiturage.Services.TrajetService;
import com.example.covoiturage.Services.UtilisateurService;
import com.example.covoiturage.ServicesImpl.AvisServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class AvisController {
    @Autowired
    private AvisService avisService;

    @RequestMapping("/evaluer")
    public ResponseEntity<String> ajouterAvis(@RequestBody AvisRequest avisRequest,
                                              @RequestParam("emailUtilisateur") String emailUtilisateur,
                                              @RequestParam("idTrajet") Long idTrajet) {

        try {
            // Appeler le service pour ajouter l'avis sans validation préalable
            Avis avis = avisService.ajouterAvis(avisRequest, emailUtilisateur, idTrajet);
            return ResponseEntity.ok("Avis ajouté avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erreur: " + e.getMessage());
        }
    }

    @GetMapping("/moyenne-avis")
    public ResponseEntity<Double> calculerMoyenneAvis(@RequestParam String email) {
        // Appeler le service pour calculer la moyenne
        Double moyenneAvis = avisService.calculerMoyenneAvis(email); // Utiliser l'instance injectée de AvisService

        if (moyenneAvis == null) {
            return ResponseEntity.notFound().build(); // Si l'utilisateur n'existe pas, retour 404
        }
        return ResponseEntity.ok(moyenneAvis); // Retourner la moyenne dans la réponse 200
    }

}
