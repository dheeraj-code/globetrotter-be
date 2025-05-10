package com.globetrotter.repository;

import com.globetrotter.model.QuizQuestion;
import com.globetrotter.model.QuizSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
    
    List<QuizQuestion> findBySession(QuizSession session);
    
    List<QuizQuestion> findBySessionAndAnswered(QuizSession session, Boolean answered);
} 