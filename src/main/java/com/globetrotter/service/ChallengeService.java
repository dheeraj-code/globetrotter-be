package com.globetrotter.service;

import com.globetrotter.model.Challenge;
import com.globetrotter.model.QuizSession;
import com.globetrotter.model.User;

import java.util.List;
import java.util.Optional;

public interface ChallengeService {
    
    Challenge createChallenge(User inviter, QuizSession inviterSession);
    
    Optional<Challenge> acceptChallenge(String inviteLink, User invitee, QuizSession inviteeSession);
    
    Optional<Challenge> getChallengeByInviteLink(String inviteLink);
    
    Optional<Challenge> getChallengeById(Long id);
    
    List<Challenge> getChallengesByInviter(User inviter);
    
    List<Challenge> getChallengesByInvitee(User invitee);
} 