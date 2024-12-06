package com.example.covoiturage.Repository;
import com.example.covoiturage.Entity.Avis;
import com.example.covoiturage.Entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvisRepository extends JpaRepository<Avis, Long> {

    // Trouver les avis d'un conducteur sur ses trajets
    List<Avis> findByTrajetConducteur(Utilisateur conducteur);

    // Trouver les avis d'un passager
    List<Avis> findByUtilisateur(Utilisateur passager);
}
