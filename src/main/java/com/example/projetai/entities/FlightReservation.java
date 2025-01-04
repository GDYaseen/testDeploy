package com.example.projetai.entities;

import com.example.projetai.enums.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;


@Entity
@Data
@Table(name = "flight_reservations")
public class FlightReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    private String clientSecretId;
    private String currency;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
    private String itineraryOrigin;       // Origine de l'itinéraire
    private String itineraryDestination;  // Destination de l'itinéraire
    private LocalDate departureDate;      // Date de départ
    private LocalDate returnDate;         // Date de retour
    private Integer adults;               // Nombre d'adultes
    private Double price;                 // Prix du vol
    private String visaNumber;            // Numéro de visa
    private String airlineName;
}
