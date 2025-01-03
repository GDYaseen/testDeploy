package com.example.projetai.controller;

import com.example.projetai.dto.AuthenticationRequest;
import com.example.projetai.dto.AuthenticationResponse;
import com.example.projetai.dto.SignupRequest;
import com.example.projetai.dto.UserDto;
import com.example.projetai.entities.User;
import com.example.projetai.repository.UserRepository;
import com.example.projetai.service.auth.AuthService;
import com.example.projetai.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
// import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {


    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    public static final  String  TOKEN_PREFIX = "Bearer" ;

    public static final String HEADER_STRING = "Authorization";
    private final AuthService authService;



    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Incorrect username or password.", HttpStatus.UNAUTHORIZED);
        }


        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            AuthenticationResponse authResponse = new AuthenticationResponse(
                    user.getId(),
                    user.getEmail(),
                    user.getName(),
                    user.getRole(),
                    Base64.getEncoder().encodeToString(user.getImg()),
                    jwt,
                    user.getCustomerStripeId()

            );
            return ResponseEntity.ok(authResponse);
        }

        return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest){
       if(authService.hasUserWithEmail(signupRequest.getEmail())){
           return new ResponseEntity<>("User already exists", HttpStatus.NOT_ACCEPTABLE);
       }

        UserDto userDto =  authService.createUser(signupRequest);
        final String jwt = jwtUtil.generateToken(userDto.getName());
        AuthenticationResponse authResponse = new AuthenticationResponse(
                    userDto.getId(),
                    userDto.getEmail(),
                    userDto.getName(),
                    userDto.getRole(),
                    userDto.getImg(),
                    jwt,
                    userDto.getCustomerStripeId()
            );
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}
