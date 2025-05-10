package com.globetrotter.repository;

import com.globetrotter.model.QuizSession;
import com.globetrotter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuizSessionRepository extends JpaRepository<QuizSession, Long> {
    
    List<QuizSession> findByUser(User user);
    
    List<QuizSession> findByUserOrderByScoreDesc(User user);
} 