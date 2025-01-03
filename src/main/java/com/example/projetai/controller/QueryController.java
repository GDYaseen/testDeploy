package com.example.projetai.controller;

// import com.example.projetai.dto.QueryRequest;
import com.example.projetai.entities.Query;
import com.example.projetai.service.QueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/queries")
public class QueryController {

    private QueryService queryService;

    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @PostMapping(path = "/create/{userId}")
    public ResponseEntity<Query> createQuery(
            @PathVariable Long userId,
            @RequestBody Map<String, String> request) {

        String queryText = request.get("query");
        if (queryText == null || queryText.isBlank()) {
            return ResponseEntity.badRequest().body(null);
        }

        Query query = queryService.addQueryToUser(userId, queryText.trim());
        return ResponseEntity.ok(query);
    }

    @GetMapping("/my-queries")
    public ResponseEntity<List<Query>> getUserQueries(@RequestParam Long userId) {
        System.out.println(userId);
        List<Query> queries = queryService.getQueriesByUserId(userId);
        return ResponseEntity.ok(queries);
    }
}
