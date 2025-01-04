package com.example.projetai.service;

import com.example.projetai.dto.HotelReservationRequest;
import com.example.projetai.entities.HotelReservation;
import com.example.projetai.entities.User;
import com.example.projetai.enums.ReservationStatus;
import com.example.projetai.repository.HotelReservationRepository;
import com.example.projetai.repository.UserRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HotelReservationService {

    @Autowired
    private HotelReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    public HotelReservation createReservation(HotelReservationRequest request) throws StripeException {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));


        HotelReservation reservation = new HotelReservation();
        reservation.setUser(user);
        reservation.setHotelId(request.getHotelId());
        reservation.setHotelName(request.getHotelName());
        reservation.setCheckInDate(request.getCheckInDate());
        reservation.setCheckOutDate(request.getCheckOutDate());
        reservation.setTotalPrice(request.getTotalPrice());
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCurrency(request.getCurrency());
        reservation = reservationRepository.save(reservation);

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (request.getTotalPrice() * 100))
                .setCurrency(request.getCurrency().toLowerCase())
                .setCustomer(user.getCustomerStripeId())
                .putMetadata("hotelReservationId", reservation.getId().toString())
                .build();
        PaymentIntent paymentIntent = PaymentIntent.create(params);

        reservation.setPaymentIntentId(paymentIntent.getClientSecret());

        return reservationRepository.save(reservation);
    }

    public List<HotelReservation> getReservationsByUser(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    public void confirmPayment(Long reservationId) throws StripeException {

        HotelReservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Réservation introuvable"));

        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);

    }


}
