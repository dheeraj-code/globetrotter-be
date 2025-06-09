package com.globetrotter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "question_pool")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionPool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "correct_city_id", nullable = false)
    private City city;

    @Column(nullable = false)
    private String clue;

    @Column(nullable = false)
    private DifficultyLevel difficultyLevel;



    public enum DifficultyLevel{
        EASY, MEDIUM, HARD;
    }

}
