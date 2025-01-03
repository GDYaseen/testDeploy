package com.example.projetai.repository;

import com.example.projetai.entities.HotelReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelReservationRepository extends JpaRepository<HotelReservation, Long> {
    List<HotelReservation> findByUserId(Long userId);
}
