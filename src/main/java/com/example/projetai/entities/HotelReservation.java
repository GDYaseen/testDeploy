package com.example.projetai.entities;

import com.example.projetai.enums.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "hotel_reservations")
public class HotelReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    private String hotelId;
    private String hotelName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalPrice;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
    private String paymentIntentId;
    private String currency;
}
