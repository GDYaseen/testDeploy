package com.example.projetai.service.auth;

import com.example.projetai.dto.SignupRequest;
import com.example.projetai.dto.UserDto;

public interface AuthService {
    UserDto createUser(SignupRequest signupRequest);
    Boolean hasUserWithEmail(String email);
}
