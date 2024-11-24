package com.example.covoiturage.Controllers;

import com.example.covoiturage.Entity.Utilisateur;
import com.example.covoiturage.Services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class UtilisateurController {
    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Utilisateur utilisateur) {
        Utilisateur utilisateurExist = utilisateurService.findByEmail(utilisateur.getEmail());
        System.out.println("Utilisateur existant : " + utilisateurExist);
        if (utilisateurExist != null && utilisateurExist.getMdp().equals(utilisateur.getMdp())) {
            System.out.println("Rôle de l'utilisateur : " + utilisateurExist.getRole());
            if ("conducteur".equalsIgnoreCase(utilisateurExist.getRole())) {
                return ResponseEntity.ok("Bienvenue Conducteur !");
            } else if ("passager".equalsIgnoreCase(utilisateurExist.getRole())) {
                return ResponseEntity.ok("Bienvenue Passager !");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Rôle inconnu !");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiants invalides");
        }
    }
}
