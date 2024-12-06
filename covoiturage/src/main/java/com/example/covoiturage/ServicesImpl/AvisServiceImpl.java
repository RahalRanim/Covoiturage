package com.example.covoiturage.ServicesImpl;
import com.example.covoiturage.Entity.Avis;
import com.example.covoiturage.Entity.AvisRequest;
import com.example.covoiturage.Entity.Trajet;
import com.example.covoiturage.Entity.Utilisateur;
import com.example.covoiturage.Repository.AvisRepository;
import com.example.covoiturage.Repository.TrajetRepository;
import com.example.covoiturage.Repository.UtilisateurRepository;
import com.example.covoiturage.Services.AvisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

@Service
public class AvisServiceImpl implements AvisService {
    @Autowired
    private AvisRepository avisRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private TrajetRepository trajetRepository;

    @Override
    public Avis ajouterAvis(AvisRequest avisRequest, String emailUtilisateur, Long idTrajet) {
        // Récupérer l'utilisateur par email
        Utilisateur utilisateur = utilisateurRepository.findByEmail(emailUtilisateur);
        if (utilisateur == null) {
            throw new RuntimeException("Utilisateur non trouvé.");
        }

        // Récupérer le trajet par ID
        Trajet trajet = trajetRepository.findById(idTrajet).orElse(null);
        if (trajet == null) {
            throw new RuntimeException("Trajet non trouvé.");
        }

        // Créer un nouvel objet Avis
        Avis avis = new Avis();
        avis.setNote(avisRequest.getNote());
        avis.setDescp(avisRequest.getDescp());
        avis.setUtilisateur(utilisateur);
        avis.setTrajet(trajet);

        // Enregistrer l'avis dans la base de données
        return avisRepository.save(avis);
    }


    @Override
    public Double calculerMoyenneAvis(String emailUtilisateur) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(emailUtilisateur);
        if (utilisateur != null) {

            // Si l'utilisateur est conducteur, les avis sont basés sur les trajets qu'il a effectués.
            if (utilisateur.isEstConducteur()) {
                List<Avis> avisList = avisRepository.findByTrajetConducteur(utilisateur);
                return calculerMoyenne(avisList);
            }
            // Si l'utilisateur est passager, les avis sont basés sur les trajets réservés par l'utilisateur.
            else if (utilisateur.isEstPassager()) {
                List<Avis> avisList = avisRepository.findByUtilisateur(utilisateur);
                return calculerMoyenne(avisList);
            }
        }
        return null; // Retourner null si l'utilisateur n'existe pas
    }

    // Méthode pour calculer la moyenne d'une liste d'avis
    private Double calculerMoyenne(List<Avis> avisList) {
        if (avisList.isEmpty()) {
            return 0.0; // Si aucune note n'est présente, la moyenne est 0
        }

        double total = 0;
        for (Avis avis : avisList) {
            total += avis.getNote();
        }

        // Calcul de la moyenne
        double moyenne = total / avisList.size();

        // Formater la moyenne avec 2 chiffres après la virgule
        DecimalFormat df = new DecimalFormat("#.00");
        String formattedMoyenne = df.format(moyenne);

        // Remplacer la virgule par un point si nécessaire et retourner la valeur formatée
        formattedMoyenne = formattedMoyenne.replace(',', '.');

        return Double.valueOf(formattedMoyenne);
    }
}
