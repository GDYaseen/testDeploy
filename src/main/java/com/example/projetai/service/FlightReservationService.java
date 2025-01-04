package com.example.projetai.service;

import com.example.projetai.dto.FlightReservationRequest;
import com.example.projetai.entities.FlightReservation;
import com.example.projetai.entities.User;
import com.example.projetai.enums.ReservationStatus;
import com.example.projetai.repository.FlightReservationRepository;
import com.example.projetai.repository.UserRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightReservationService {

    @Autowired
    private FlightReservationRepository flightReservationRepository;

    @Autowired
    private UserRepository userRepository;

    public FlightReservation createReservation(FlightReservationRequest request) throws StripeException, StripeException {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        FlightReservation reservation = new FlightReservation();
        reservation.setUser(user);
        reservation.setItineraryOrigin(request.getItineraryOrigin());
        reservation.setItineraryDestination(request.getItineraryDestination());
        reservation.setDepartureDate(request.getDepartureDate());
        reservation.setReturnDate(request.getReturnDate());
        reservation.setAdults(request.getAdults());
        reservation.setPrice(request.getPrice());
        reservation.setVisaNumber(request.getVisaNumber());
        reservation.setCurrency(request.getCurrency());
        reservation.setAirlineName(request.getAirlineName());
        reservation.setStatus(ReservationStatus.PENDING);

        reservation = flightReservationRepository.save(reservation);

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (request.getPrice() * 100))
                .setCurrency(request.getCurrency().toLowerCase())
                .setCustomer(user.getCustomerStripeId())
                .putMetadata("flightReservationId", reservation.getId().toString())
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        reservation.setClientSecretId(paymentIntent.getClientSecret());

        return flightReservationRepository.save(reservation);
    }

    public List<FlightReservation> getReservationsByUser(Long userId) {
        return flightReservationRepository.findByUserId(userId);
    }

    public void confirmPayment(Long reservationId) throws StripeException {

        FlightReservation reservation = flightReservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Réservation introuvable"));

        reservation.setStatus(ReservationStatus.CONFIRMED);
        flightReservationRepository.save(reservation);
    }
}
