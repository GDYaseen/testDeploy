package com.example.projetai;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProjetAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjetAiApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(){
        return args -> {};
    }

}
