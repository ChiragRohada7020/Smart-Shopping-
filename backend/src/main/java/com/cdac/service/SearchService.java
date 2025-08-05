package com.cdac.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdac.dto.SearchRequest;
import com.cdac.entity.SearchHistory;
import com.cdac.entity.User;
import com.cdac.repository.SearchHistoryRepository;
import com.cdac.repository.UserRepository;

@Service
public class SearchService {

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    public void logSearch(Long userId, String query) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            SearchHistory history = new SearchHistory();
            history.setSearchQuery(query);
            history.setSearchedAt(LocalDateTime.now());
            history.setUser(user);
            searchHistoryRepository.save(history);
        }
    }

    public List<SearchHistory> getSearchHistory(Long userId) {
        return searchHistoryRepository.findByUser_Id(userId);
    }
}
