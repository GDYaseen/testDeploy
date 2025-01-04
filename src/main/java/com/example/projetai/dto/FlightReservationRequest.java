package com.example.projetai.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FlightReservationRequest {
    private Long userId;
    private String itineraryOrigin;
    private String itineraryDestination;
    private LocalDate departureDate;
    private LocalDate returnDate;
    private Integer adults;
    private Double price;
    private String currency;
    private String visaNumber;
    private String airlineName;
}
