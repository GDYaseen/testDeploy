package com.example.projetai.repository;

import com.example.projetai.entities.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueryRepository extends JpaRepository<Query,Long> {
    List<Query> findByUserId(Long userId);
}
