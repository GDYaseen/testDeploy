package com.example.projetai.dto;

import com.example.projetai.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String email;
    private String name ;
    private UserRole role ;
    private String customerStripeId;
    private String img ;

}
