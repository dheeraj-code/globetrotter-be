package com.globetrotter.controller;

import com.globetrotter.model.Challenge;
import com.globetrotter.model.QuizSession;
import com.globetrotter.model.User;
import com.globetrotter.service.ChallengeService;
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

@RestController
@RequestMapping("/challenge")
public class ChallengeController {

    private final ChallengeService challengeService;
    private final UserService userService;
    private final QuizService quizService;

    @Autowired
    public ChallengeController(ChallengeService challengeService, UserService userService, QuizService quizService) {
        this.challengeService = challengeService;
        this.userService = userService;
        this.quizService = quizService;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createChallenge(@RequestBody Map<String, Object> payload) {
        try {
            // Get the authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            
            Optional<User> userOpt = userService.findByEmail(email);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            Long sessionId = Long.parseLong(payload.get("sessionId").toString());
            Optional<QuizSession> sessionOpt = quizService.getQuizResult(sessionId);
            
            if (sessionOpt.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            Challenge challenge = challengeService.createChallenge(userOpt.get(), sessionOpt.get());
            
            Map<String, Object> response = new HashMap<>();
            response.put("challengeId", challenge.getId());
            response.put("inviteLink", challenge.getInviteLink());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/accept/{inviteLink}")
    public ResponseEntity<Challenge> acceptChallenge(@PathVariable String inviteLink) {
        try {
            // Get the authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            
            Optional<User> userOpt = userService.findByEmail(email);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            // Start a new quiz session for the invitee
            QuizSession inviteeSession = quizService.startQuizSession(userOpt.get());
            
            Optional<Challenge> challengeOpt = challengeService.acceptChallenge(
                    inviteLink, userOpt.get(), inviteeSession);
            
            return challengeOpt
                    .map(challenge -> ResponseEntity.ok(challenge))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/result/{challengeId}")
    public ResponseEntity<Map<String, Object>> getChallengeResult(@PathVariable Long challengeId) {
        Optional<Challenge> challengeOpt = challengeService.getChallengeById(challengeId);
        
        if (challengeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Challenge challenge = challengeOpt.get();
        Map<String, Object> result = new HashMap<>();
        
        result.put("challengeId", challenge.getId());
        result.put("inviter", challenge.getInviter().getUsername());
        result.put("inviterScore", challenge.getInviterSession().getScore());
        result.put("inviterTime", challenge.getInviterSession().getTimeTaken());
        
        if (challenge.getStatus() == Challenge.ChallengeStatus.ACCEPTED && 
                challenge.getInvitee() != null && 
                challenge.getInviteeSession() != null) {
            result.put("invitee", challenge.getInvitee().getUsername());
            result.put("inviteeScore", challenge.getInviteeSession().getScore());
            result.put("inviteeTime", challenge.getInviteeSession().getTimeTaken());
            
            // Determine the winner based on score and time
            String winner;
            int inviterScore = challenge.getInviterSession().getScore();
            int inviteeScore = challenge.getInviteeSession().getScore();
            
            if (inviterScore > inviteeScore) {
                winner = challenge.getInviter().getUsername();
            } else if (inviteeScore > inviterScore) {
                winner = challenge.getInvitee().getUsername();
            } else {
                // If scores are tied, the player with less time wins
                int inviterTime = challenge.getInviterSession().getTimeTaken();
                int inviteeTime = challenge.getInviteeSession().getTimeTaken();
                
                if (inviterTime < inviteeTime) {
                    winner = challenge.getInviter().getUsername();
                } else if (inviteeTime < inviterTime) {
                    winner = challenge.getInvitee().getUsername();
                } else {
                    winner = "tie";
                }
            }
            
            result.put("winner", winner);
        } else {
            result.put("status", "pending");
        }
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{inviteLink}")
    public ResponseEntity<?> getChallengeByLink(@PathVariable String inviteLink, Authentication authentication) {
        try {
            // Get the current user from authentication
            String email = authentication.getName();
            Optional<User> userOpt = userService.findByEmail(email);
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "User not authenticated"));
            }
            
            User currentUser = userOpt.get();
            
            // Find challenge by invite link
            Challenge challenge = challengeService.getChallengeByInviteLink(inviteLink);
            
            if (challenge == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Challenge not found"));
            }
            
            // Get challenge with full details
            Challenge challengeDetails = challengeService.getChallengeWithDetails(challenge.getId());
            
            // Add additional properties for the response
            Map<String, Object> response = new HashMap<>();
            response.put("id", challengeDetails.getId());
            response.put("inviterId", challengeDetails.getInviter().getId());
            response.put("inviterUsername", challengeDetails.getInviter().getUsername());
            response.put("inviterScore", challengeDetails.getInviterSession() != null ? 
                    challengeDetails.getInviterSession().getScore() : null);
            response.put("inviteeId", challengeDetails.getInvitee() != null ? 
                    challengeDetails.getInvitee().getId() : null);
            response.put("inviteeUsername", challengeDetails.getInvitee() != null ? 
                    challengeDetails.getInvitee().getUsername() : null);
            response.put("inviteeScore", challengeDetails.getInviteeSession() != null ? 
                    challengeDetails.getInviteeSession().getScore() : null);
            response.put("status", challengeDetails.getStatus());
            response.put("inviteLink", challengeDetails.getInviteLink());
            response.put("createdAt", challengeDetails.getCreatedAt());
            
            // Check if this is the user's own challenge
            if (challengeDetails.getInviter().getId().equals(currentUser.getId())) {
                response.put("isOwnChallenge", true);
            }
            
            // Check if challenge is already accepted by the current user
            if ("accepted".equals(challengeDetails.getStatus().toString()) && 
                    challengeDetails.getInvitee() != null && 
                    challengeDetails.getInvitee().getId().equals(currentUser.getId())) {
                response.put("isAlreadyAccepted", true);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to get challenge details"));
        }
    }

    @GetMapping("/my-challenges")
    public ResponseEntity<List<Challenge>> getMyChallenges() {
        // Get the authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        
        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<Challenge> inviterChallenges = challengeService.getChallengesByInviter(userOpt.get());
        List<Challenge> inviteeChallenges = challengeService.getChallengesByInvitee(userOpt.get());
        
        // Combine both lists
        inviterChallenges.addAll(inviteeChallenges);
        
        return ResponseEntity.ok(inviterChallenges);
    }
} 