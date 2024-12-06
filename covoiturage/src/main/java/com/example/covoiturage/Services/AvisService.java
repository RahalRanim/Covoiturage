package com.example.covoiturage.Services;
import com.example.covoiturage.Entity.Avis;
import com.example.covoiturage.Entity.AvisRequest;
import com.example.covoiturage.Entity.Trajet;
import com.example.covoiturage.Entity.Utilisateur;

public interface AvisService {
    Avis ajouterAvis(AvisRequest avisRequest, String emailUtilisateur, Long idTrajet);
    Double calculerMoyenneAvis(String emailUtilisateur);
}
