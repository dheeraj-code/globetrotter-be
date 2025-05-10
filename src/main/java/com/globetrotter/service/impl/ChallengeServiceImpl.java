package com.globetrotter.service.impl;

import com.globetrotter.model.Challenge;
import com.globetrotter.model.QuizSession;
import com.globetrotter.model.User;
import com.globetrotter.repository.ChallengeRepository;
import com.globetrotter.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeRepository challengeRepository;

    @Autowired
    public ChallengeServiceImpl(ChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
    }

    @Override
    @Transactional
    public Challenge createChallenge(User inviter, QuizSession inviterSession) {
        Challenge challenge = new Challenge();
        challenge.setInviter(inviter);
        challenge.setInviterSession(inviterSession);
        
        // Generate a unique invite link
        String inviteLink = UUID.randomUUID().toString();
        challenge.setInviteLink(inviteLink);
        
        return challengeRepository.save(challenge);
    }

    @Override
    @Transactional
    public Optional<Challenge> acceptChallenge(String inviteLink, User invitee, QuizSession inviteeSession) {
        Optional<Challenge> challengeOpt = challengeRepository.findByInviteLink(inviteLink);
        
        if (challengeOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Challenge challenge = challengeOpt.get();
        
        // Set the invitee and their session
        challenge.setInvitee(invitee);
        challenge.setInviteeSession(inviteeSession);
        challenge.setStatus(Challenge.ChallengeStatus.ACCEPTED);
        
        return Optional.of(challengeRepository.save(challenge));
    }

    @Override
    public Optional<Challenge> getChallengeByInviteLink(String inviteLink) {
        return challengeRepository.findByInviteLink(inviteLink);
    }

    @Override
    public Optional<Challenge> getChallengeById(Long id) {
        return challengeRepository.findById(id);
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