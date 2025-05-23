package com.globetrotter.service.impl;

import com.globetrotter.model.City;
import com.globetrotter.model.QuizQuestion;
import com.globetrotter.model.QuizSession;
import com.globetrotter.model.User;
import com.globetrotter.repository.QuizQuestionRepository;
import com.globetrotter.repository.QuizSessionRepository;
import com.globetrotter.repository.UserRepository;
import com.globetrotter.service.CityService;
import com.globetrotter.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class QuizServiceImpl implements QuizService {

    private final QuizSessionRepository quizSessionRepository;
    private final UserRepository userRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final CityService cityService;

    @Autowired
    public QuizServiceImpl(
            QuizSessionRepository quizSessionRepository, 
            UserRepository userRepository,
            QuizQuestionRepository quizQuestionRepository,
            CityService cityService) {
        this.quizSessionRepository = quizSessionRepository;
        this.userRepository = userRepository;
        this.quizQuestionRepository = quizQuestionRepository;
        this.cityService = cityService;
    }

    @Override
    @Transactional
    public QuizSession startQuizSession(User user) {
        QuizSession session = new QuizSession();
        session.setUser(user);
        session.setStartTime(LocalDateTime.now());
        return quizSessionRepository.save(session);
    }

    @Override
    @Transactional
    public QuizSession submitAnswer(Long sessionId, Map<String, Object> answerData) {
        Optional<QuizSession> sessionOpt = quizSessionRepository.findById(sessionId);
        
        if (sessionOpt.isEmpty()) {
            throw new IllegalArgumentException("Quiz session not found");
        }
        
        QuizSession session = sessionOpt.get();
        
        // Extract data from the answer
        boolean isCorrect = (boolean) answerData.getOrDefault("isCorrect", false);
        int timeTaken = (int) answerData.getOrDefault("timeTaken", 0);
        
        // Update score if the answer is correct
        if (isCorrect) {
            int currentScore = session.getScore() != null ? session.getScore() : 0;
            session.setScore(currentScore + 1);
        }
        
        // Update time taken
        int currentTimeTaken = session.getTimeTaken() != null ? session.getTimeTaken() : 0;
        session.setTimeTaken(currentTimeTaken + timeTaken);
        
        return quizSessionRepository.save(session);
    }

    @Override
    public Optional<QuizSession> getQuizResult(Long sessionId) {
        return quizSessionRepository.findById(sessionId);
    }

    @Override
    @Transactional
    public void endQuizSession(QuizSession session, int score, int timeTaken) {
        session.setEndTime(LocalDateTime.now());
        session.setScore(score);
        session.setTimeTaken(timeTaken);
        
        // Update user's total score
        User user = session.getUser();
        user.setScore(user.getScore() + score);
        userRepository.save(user);
        
        quizSessionRepository.save(session);
    }
    
    @Override
    @Transactional
    public QuizQuestion createRandomQuestion(Long sessionId) {
        // Verify session exists and belongs to the user
        Optional<QuizSession> sessionOpt = quizSessionRepository.findById(sessionId);
        if (sessionOpt.isEmpty()) {
            throw new IllegalArgumentException("Quiz session not found with ID: " + sessionId);
        }
        
        QuizSession session = sessionOpt.get();
        
        // Get a random city as the correct answer
        City correctCity = cityService.getRandomCity();
        String clue = cityService.getRandomClue(correctCity.getId());
        
        // Create 3 random options (excluding the correct city)
        List<City> options = cityService.getRandomOptions(correctCity.getId(), 3);
        
        // Create and save the quiz question
        QuizQuestion question = new QuizQuestion();
        question.setSession(session);
        question.setCorrectCity(correctCity);
        
        // Add the correct city to the options
        options.add(correctCity);
        
        // Add all options to the question
        for (City option : options) {
            question.addOption(option);
        }
        
        // Save the question
        return quizQuestionRepository.save(question);
    }
} 