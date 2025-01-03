package com.example.projetai.controller;

import com.example.projetai.dto.ReservationRequest;
import com.example.projetai.entities.HotelReservation;
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
@RequestMapping("/api/reservations")
@CrossOrigin("*")
public class HotelReservationController {

    private static final String endpointSecret = "whsec_U4mPq7khjBEMMBjLRalO4WZF4UWXAjkd";
    @Autowired
    private HotelReservationService reservationService;

    @PostMapping
    public HotelReservation createReservation(@RequestBody ReservationRequest request) throws StripeException {
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
                String paymentIntentId = paymentIntent.getId();

                String reservationId = paymentIntent.getMetadata().get("reservationId");
                if (reservationId != null) {
                    Long reservationIdLong = Long.parseLong(reservationId);
                    reservationService.confirmPayment(reservationIdLong);
                }
            }
        }
    }
}
