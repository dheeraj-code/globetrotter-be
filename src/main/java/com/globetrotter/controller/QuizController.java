package com.globetrotter.controller;

import com.globetrotter.model.City;
import com.globetrotter.model.QuizQuestion;
import com.globetrotter.model.QuizSession;
import com.globetrotter.model.User;
import com.globetrotter.service.CityService;
import com.globetrotter.service.QuizService;
import com.globetrotter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;
    private final UserService userService;
    private final CityService cityService;

    @Autowired
    public QuizController(QuizService quizService, UserService userService, CityService cityService) {
        this.quizService = quizService;
        this.userService = userService;
        this.cityService = cityService;
    }

    @PostMapping("/start")
    public ResponseEntity<QuizSession> startQuiz() {
        // Get the authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        
        // Find the user
        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        // Start a new quiz session
        QuizSession session = quizService.startQuizSession(userOpt.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(session);
    }

    @PostMapping("/answer")
    public ResponseEntity<QuizSession> submitAnswer(@RequestBody Map<String, Object> payload) {
        try {
            Long sessionId = Long.parseLong(payload.get("sessionId").toString());
            
            QuizSession session = quizService.submitAnswer(sessionId, payload);
            return ResponseEntity.ok(session);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/end")
    public ResponseEntity<QuizSession> endQuiz(@RequestBody Map<String, Object> payload) {
        try {
            Long sessionId = Long.parseLong(payload.get("sessionId").toString());
            int score = Integer.parseInt(payload.get("score").toString());
            int timeTaken = Integer.parseInt(payload.get("timeTaken").toString());
            
            Optional<QuizSession> sessionOpt = quizService.getQuizResult(sessionId);
            if (sessionOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            quizService.endQuizSession(sessionOpt.get(), score, timeTaken);
            return ResponseEntity.ok(sessionOpt.get());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/result/{sessionId}")
    public ResponseEntity<QuizSession> getQuizResult(@PathVariable Long sessionId) {
        return quizService.getQuizResult(sessionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/random/{sessionId}")
    public ResponseEntity<Map<String, Object>> getRandomQuiz(@PathVariable Long sessionId) {
        try {
            // Get the authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            
            // Find the user
            Optional<User> userOpt = userService.findByEmail(email);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            // Verify the session exists and belongs to this user
            Optional<QuizSession> sessionOpt = quizService.getQuizResult(sessionId);
            if (sessionOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Quiz session not found."));
            }
            
            QuizSession session = sessionOpt.get();
            if (!session.getUser().getId().equals(userOpt.get().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Access denied."));
            }
            
            // Create a random quiz question
            QuizQuestion question = quizService.createRandomQuestion(sessionId);
            
            // Get a random clue for the correct city
            String clue = cityService.getRandomClue(question.getCorrectCity().getId());
            
            // Format the response
            Map<String, Object> response = new HashMap<>();
            response.put("id", question.getId());
            response.put("cityId", question.getCorrectCity().getId());
            response.put("clue", clue);
            
            List<Map<String, Object>> formattedOptions = question.getOptions().stream()
                .map(city -> {
                    Map<String, Object> option = new HashMap<>();
                    option.put("id", city.getId());
                    option.put("city", city.getCity());
                    option.put("country", city.getCountry());
                    return option;
                })
                .collect(Collectors.toList());
            
            response.put("options", formattedOptions);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Error fetching quiz: " + e.getMessage()));
        }
    }
} 