package com.example.projetai.repository;

import com.example.projetai.entities.FlightReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightReservationRepository extends JpaRepository<FlightReservation,Long> {
    List<FlightReservation> findByUserId(Long userId);
}
