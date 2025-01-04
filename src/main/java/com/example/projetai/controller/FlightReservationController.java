package com.example.projetai.controller;

import com.example.projetai.dto.FlightReservationRequest;
import com.example.projetai.entities.FlightReservation;
import com.example.projetai.service.FlightReservationService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flightReservations")
@CrossOrigin("*")
public class FlightReservationController {

    @Autowired
    private FlightReservationService flightReservationService;

    @PostMapping
    public FlightReservation createReservation(@RequestBody FlightReservationRequest request) throws StripeException {
        // System.out.println(request.getUserId());
        // System.out.println(request.getItineraryOrigin());
        // System.out.println(request.getItineraryDestination());
        // System.out.println(request.getDepartureDate());
        // System.out.println(request.getReturnDate());
        // System.out.println(request.getAdults());
        // System.out.println(request.getPrice());
        // System.out.println(request.getVisaNumber());
        // System.out.println(request.getCurrency());
        // System.out.println(request.getAirlineName());
        // return null;
        return flightReservationService.createReservation(request);
    }

    @GetMapping("/{userId}")
    public List<FlightReservation> getReservationsByUser(@PathVariable Long userId) {
        return flightReservationService.getReservationsByUser(userId);
    }


}
