package com.example.projetai.service.auth;


import com.example.projetai.dto.SignupRequest;
import com.example.projetai.dto.UserDto;

import com.example.projetai.entities.User;

import com.example.projetai.enums.UserRole;

import com.example.projetai.repository.UserRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import jakarta.annotation.PostConstruct;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    // @Autowired
    // private BCryptPasswordEncoder bCryptPasswordEncoder;



    public UserDto createUser(SignupRequest signupRequest){
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setName(signupRequest.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setRole(UserRole.USER);

        // Decode Base64 string to byte[]
        if (signupRequest.getImg() != null && !signupRequest.getImg().isEmpty()) {
            byte[] decodedImg = Base64.getDecoder().decode(signupRequest.getImg());
            user.setImg(decodedImg);
        }
        User createdUser=null;
        try {
            Stripe.apiKey = "sk_test_51QcGKUFTXQXhxwtldMmtJCTBYMjIPA6X6BJG4GltlYunzhBXvILfXJj6eoWWUola18VIiots9v4ZtUaURc9DlFxz00kO6OiPgV";
            Customer customer = Customer.create(
                    Map.of(
                            "email", signupRequest.getEmail(),
                            "name", signupRequest.getName()
                    )
            );
            user.setCustomerStripeId(customer.getId());

            createdUser = userRepository.save(user);

            UserDto userDto = new UserDto();
            userDto.setId(createdUser.getId());
            userDto.setEmail(signupRequest.getEmail());
            userDto.setName(signupRequest.getName());
            userDto.setRole(UserRole.USER);
            userDto.setCustomerStripeId(createdUser.getCustomerStripeId());
            userDto.setImg(signupRequest.getImg());
            return userDto;

        } catch (StripeException e) {
            throw new RuntimeException("Erreur lors de la création du client Stripe : " + e.getMessage());
        }

    }

    public Boolean hasUserWithEmail(String email){
      return   userRepository.findFirstByEmail(email).isPresent();
    }

    @PostConstruct
    public void createAdminAccount(){
        User adminAccount = userRepository.findByRole(UserRole.ADMIN);
        if(null == adminAccount){
            User user = new User();
            user.setEmail("admin@test.com");
            user.setName("admin");
            user.setRole(UserRole.ADMIN);
            user.setPassword( new BCryptPasswordEncoder().encode("admin"));
            userRepository.save(user);
        }
    }
}
