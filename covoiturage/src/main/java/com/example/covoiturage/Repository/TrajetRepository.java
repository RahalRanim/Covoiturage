package com.example.covoiturage.Repository;

import com.example.covoiturage.Entity.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrajetRepository extends JpaRepository<Trajet, Long> {
}
