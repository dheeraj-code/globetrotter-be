package com.globetrotter.service;

import com.globetrotter.model.QuizQuestion;
import com.globetrotter.model.QuizSession;
import com.globetrotter.model.User;

import java.util.Map;
import java.util.Optional;

public interface QuizService {
    
    QuizSession startQuizSession(User user);
    
    QuizSession submitAnswer(Long sessionId, Map<String, Object> answerData);
    
    Optional<QuizSession> getQuizResult(Long sessionId);
    
    void endQuizSession(QuizSession session, int score, int timeTaken);
    
    QuizQuestion createRandomQuestion(Long sessionId);
} 