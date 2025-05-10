package com.globetrotter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz_question")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizQuestion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private QuizSession session;
    
    @ManyToOne
    @JoinColumn(name = "correct_city_id", nullable = false)
    private City correctCity;
    
    @ManyToMany
    @JoinTable(
        name = "quiz_question_options",
        joinColumns = @JoinColumn(name = "question_id"),
        inverseJoinColumns = @JoinColumn(name = "city_id")
    )
    private List<City> options = new ArrayList<>();
    
    private Boolean answered = false;
    
    private Boolean correctlyAnswered = false;
    
    public void addOption(City city) {
        this.options.add(city);
    }
} 