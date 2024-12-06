package com.example.covoiturage.ServicesImpl;

import com.example.covoiturage.Entity.Trajet;
import com.example.covoiturage.Entity.Utilisateur;
import com.example.covoiturage.Repository.TrajetRepository;
import com.example.covoiturage.Services.TrajetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class TrajetServiceImpl implements TrajetService {
    @Autowired
    private TrajetRepository trajetRepository;


    @Override
    public Trajet ajouterTrajet(Trajet trajet, Utilisateur conducteur) {
        // Vérifier si un trajet à la même date et heure existe déjà pour ce conducteur
        if (trajetExiste(trajet, conducteur)) {
            throw new IllegalArgumentException("Un trajet existe déjà pour ce conducteur à cette date et heure.");
        }

        trajet.setConducteur(conducteur); // Associer le conducteur au trajet
        return trajetRepository.save(trajet); // Enregistrer le trajet
    }

    @Override
    public List<Trajet> afficherTrajetsParConducteur(Utilisateur conducteur) {
        return trajetRepository.findByConducteur(conducteur); // Utilise l'ID du conducteur automatiquement
    }



    @Override
    public boolean trajetExiste(Trajet trajet, Utilisateur conducteur) {
        // Récupérer les trajets existants pour le même conducteur à la même date et heure
        List<Trajet> trajetsExistants = trajetRepository.veriftrajet(conducteur, trajet.getDate(), trajet.getTime());

        // Log ou debug pour voir ce que contient trajetsExistants
        System.out.println("Trajets existants : " + trajetsExistants.size());

        // Si la liste est vide, aucun trajet similaire n'existe
        return !trajetsExistants.isEmpty();
    }

    @Override
    public List<Trajet> rechercherTrajets(String depart, String destination, LocalDate date, LocalTime time, Double prixMax, String nomConducteur, String prenomConducteur) {
        // Appel au repository pour effectuer la recherche
        return trajetRepository.rechercheAvanceeTrajets(depart, destination, date, time, prixMax, nomConducteur, prenomConducteur);
    }

    @Override
    public List<Trajet> getTrajetsDisponiblesPourPassager() {
        return trajetRepository.findByPlaceDispoGreaterThan(0); // Utilisation de la méthode optimisée du repository
    }

    @Override
    public Trajet getTrajetById(Long trajetId) {
        return trajetRepository.findById(trajetId)
                .orElseThrow(() -> new IllegalArgumentException("Trajet introuvable."));
    }

    @Override
    public boolean isTrajetStillValid(Long trajetId) {
        Trajet trajet = trajetRepository.findById(trajetId)
                .orElseThrow(() -> new RuntimeException("Trajet introuvable avec l'ID : " + trajetId));

        // Comparer la date du trajet avec la date actuelle
        return trajet.getDate().isBefore(LocalDate.now());
    }




}
