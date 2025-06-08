package com.globetrotter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "question_pool")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionPool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, name = "city_id")
    private City city;
    @Column(nullable = false, name = "clue")
    private String clue;
    @Column(name = "last_used")
    private LocalDateTime lastUsed;
    @Column(name = "time_used")
    private Integer timesUsed = 0;
}
