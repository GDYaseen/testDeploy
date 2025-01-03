package com.example.projetai.service;

import com.example.projetai.entities.Query;
import com.example.projetai.entities.User;
import com.example.projetai.repository.QueryRepository;
import com.example.projetai.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QueryService {

    private UserRepository userRepository;
    private QueryRepository queryRepository;

    public QueryService(UserRepository userRepository, QueryRepository queryRepository) {
        this.userRepository = userRepository;
        this.queryRepository = queryRepository;
    }

    public Query addQueryToUser(Long userId, String queryText) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Query query = new Query();
        query.setQueryText(queryText.trim());
        query.setUser(user);
        query.setCreatedAt(LocalDateTime.now());

        return queryRepository.save(query);
    }

    public List<Query> getQueriesByUserId(Long userId) {
        return queryRepository.findByUserId(userId);
    }
}

