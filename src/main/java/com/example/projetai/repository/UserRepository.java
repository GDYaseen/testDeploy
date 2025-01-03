package com.example.projetai.repository;

import com.example.projetai.entities.User;
import com.example.projetai.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findFirstByEmail(String username);

    User findByRole(UserRole userRole);
}
