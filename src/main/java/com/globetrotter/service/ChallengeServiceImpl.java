package com.globetrotter.service;

import com.globetrotter.model.Challenge;
import com.globetrotter.model.QuizSession;
import com.globetrotter.model.User;
import com.globetrotter.repository.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDateTime;

@Service
public class ChallengeServiceImpl implements ChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Override
    public Challenge createChallenge(User inviter, QuizSession inviterSession) {
        Challenge challenge = new Challenge();
        challenge.setInviter(inviter);
        challenge.setInviterSession(inviterSession);
        challenge.setStatus(Challenge.ChallengeStatus.PENDING);
        challenge.setInviteLink(UUID.randomUUID().toString());
        challenge.setCreatedAt(LocalDateTime.now());
        
        return challengeRepository.save(challenge);
    }
    
    @Override
    public Optional<Challenge> acceptChallenge(String inviteLink, User invitee, QuizSession inviteeSession) {
        Optional<Challenge> challengeOpt = challengeRepository.findByInviteLink(inviteLink);
        
        if (challengeOpt.isPresent()) {
            Challenge challenge = challengeOpt.get();
            challenge.setInvitee(invitee);
            challenge.setInviteeSession(inviteeSession);
            challenge.setStatus(Challenge.ChallengeStatus.ACCEPTED);
            
            return Optional.of(challengeRepository.save(challenge));
        }
        
        return Optional.empty();
    }

    @Override
    public Challenge getChallengeByInviteLink(String inviteLink) {
        return challengeRepository.findByInviteLink(inviteLink)
                .orElse(null);
    }
    
    @Override
    public Optional<Challenge> getChallengeById(Long id) {
        return challengeRepository.findById(id);
    }

    @Override
    public Challenge getChallengeWithDetails(Long challengeId) {
        return challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("Challenge not found"));
    }
    
    @Override
    public List<Challenge> getChallengesByInviter(User inviter) {
        return challengeRepository.findByInviter(inviter);
    }
    
    @Override
    public List<Challenge> getChallengesByInvitee(User invitee) {
        return challengeRepository.findByInvitee(invitee);
    }
} 