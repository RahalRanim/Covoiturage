package com.example.covoiturage.Repository;

import com.example.covoiturage.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
