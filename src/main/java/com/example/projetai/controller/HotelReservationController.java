package com.example.projetai.controller;

import com.example.projetai.dto.HotelReservationRequest;
import com.example.projetai.entities.HotelReservation;
import com.example.projetai.service.FlightReservationService;
import com.example.projetai.service.HotelReservationService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/reservations")
@CrossOrigin("*")
public class HotelReservationController {

    private static final String endpointSecret = "whsec_U4mPq7khjBEMMBjLRalO4WZF4UWXAjkd";
    @Autowired
    private HotelReservationService reservationService;
    @Autowired
    private FlightReservationService flightReservationService;

    @PostMapping
    public HotelReservation createReservation(@RequestBody HotelReservationRequest request) throws StripeException {

        return reservationService.createReservation(request);
    }

    @GetMapping("/{userId}")
    public List<HotelReservation> getReservationsByUser(@PathVariable Long userId) {
        return reservationService.getReservationsByUser(userId);
    }

    @PostMapping("/payment")
    public void handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) throws StripeException {
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            throw new IllegalArgumentException("Webhook signature verification failed.");
        }

        if ("payment_intent.succeeded".equals(event.getType())) {
            PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);

            if (paymentIntent != null) {

                String hotelReservationId = paymentIntent.getMetadata().get("hotelReservationId");
                if (hotelReservationId != null) {
                    Long reservationIdLong = Long.parseLong(hotelReservationId);
                    reservationService.confirmPayment(reservationIdLong);
                }
                String flightReservationId = paymentIntent.getMetadata().get("flightReservationId");
                if (flightReservationId != null) {
                    Long reservationIdLong = Long.parseLong(flightReservationId);
                    flightReservationService.confirmPayment(reservationIdLong);
                }
            }
        }
    }
}
