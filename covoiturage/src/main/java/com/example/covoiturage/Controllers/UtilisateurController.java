package com.example.covoiturage.Controllers;

import com.example.covoiturage.Entity.Utilisateur;
import com.example.covoiturage.Services.UtilisateurService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    // Injection par constructeur recommandée
    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Utilisateur utilisateur, HttpSession session) {
        Utilisateur utilisateurExist = utilisateurService.findByEmail(utilisateur.getEmail());

        if (utilisateurExist != null && utilisateurExist.getMdp().equals(utilisateur.getMdp())) {
            String role = utilisateurExist.getRole();
            if ("conducteur".equalsIgnoreCase(role)) {
                session.setAttribute("currentUser", utilisateurExist);
                return ResponseEntity.ok(new LoginResponse("conducteur"));
            } else if ("passager".equalsIgnoreCase(role)) {
                return ResponseEntity.ok(new LoginResponse("passager"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse("Rôle inconnu"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("Identifiants invalides"));
        }
    }

    // Utilisation d'un record pour simplifier la classe
    public record LoginResponse(String message) {}
}
