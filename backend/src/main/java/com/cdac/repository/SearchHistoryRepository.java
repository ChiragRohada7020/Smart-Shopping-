package com.cdac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cdac.entity.SearchHistory;
import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findByUser_Id(Long userId); 
}
