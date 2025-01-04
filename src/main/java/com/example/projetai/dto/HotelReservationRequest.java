package com.example.projetai.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Setter
@Getter
public class HotelReservationRequest {
    private Long userId;
    private String hotelId;
    private String hotelName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String currency;
    private double totalPrice;
}
