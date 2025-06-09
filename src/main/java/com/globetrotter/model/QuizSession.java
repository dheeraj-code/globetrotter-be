package com.globetrotter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_session")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizSession {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(name = "time_taken")
    private Integer timeTaken = 0;
    
    @Column
    private Integer score = 0;
    @Column
    private QuestionPool.DifficultyLevel currentLevel;
} 