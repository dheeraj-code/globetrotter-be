package com.globetrotter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "challenge")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Challenge {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "inviter_session_id", nullable = false)
    private QuizSession inviterSession;
    
    @ManyToOne
    @JoinColumn(name = "inviter_id", nullable = false)
    private User inviter;
    
    @ManyToOne
    @JoinColumn(name = "invitee_id")
    private User invitee;
    
    @ManyToOne
    @JoinColumn(name = "invitee_session_id")
    private QuizSession inviteeSession;
    
    @Column(name = "invite_link", nullable = false)
    private String inviteLink;
    
    @Enumerated(EnumType.STRING)
    private ChallengeStatus status = ChallengeStatus.PENDING;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    public enum ChallengeStatus {
        PENDING, ACCEPTED
    }
} 