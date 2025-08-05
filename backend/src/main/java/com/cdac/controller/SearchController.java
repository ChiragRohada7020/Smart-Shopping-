package com.cdac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cdac.dto.SearchRequest;
import com.cdac.service.SearchService;
import jakarta.validation.Valid;
import java.util.List;
import com.cdac.entity.SearchHistory;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping
    public ResponseEntity<String> searchProduct(@Valid @RequestBody SearchRequest request) {
        searchService.logSearch(request.getUserId(), request.getQuery());

        String result = "Showing results for: " + request.getQuery();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<SearchHistory>> getUserSearchHistory(@PathVariable Long userId) {
        List<SearchHistory> history = searchService.getSearchHistory(userId);
        return ResponseEntity.ok(history);
    }
}
