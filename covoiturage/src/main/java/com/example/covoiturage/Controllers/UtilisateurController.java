package com.example.covoiturage.Controllers;

import com.example.covoiturage.Entity.Utilisateur;
import com.example.covoiturage.Services.UtilisateurService;
import jakarta.servlet.http.HttpSession;
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
    public ResponseEntity<?> login(@RequestBody Utilisateur utilisateur, HttpSession session) {
        Utilisateur utilisateurExist = utilisateurService.findByEmail(utilisateur.getEmail());

        if (utilisateurExist != null && utilisateurExist.getMdp().equals(utilisateur.getMdp())) {
            if ("conducteur".equalsIgnoreCase(utilisateurExist.getRole())) {
                session.setAttribute("currentUser", utilisateurExist);
                return ResponseEntity.ok(new LoginResponse("conducteur"));
            } else if ("passager".equalsIgnoreCase(utilisateurExist.getRole())) {
                return ResponseEntity.ok(new LoginResponse("passager"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse("Rôle inconnu"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("Identifiants invalides"));
        }
    }

    public static class LoginResponse {
        private String message;

        public LoginResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }


}
