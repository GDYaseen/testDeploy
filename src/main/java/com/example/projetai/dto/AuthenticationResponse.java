package com.example.projetai.dto;

import com.example.projetai.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private Long id;
    private String email;
    private String name;
    private UserRole role;
    private String img;
    private String token;
    private String customerStripeId;
}