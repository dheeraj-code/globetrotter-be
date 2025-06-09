package com.globetrotter.repository;

import com.globetrotter.model.QuestionPool;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionPoolRepository extends JpaRepository<QuestionPool, Long> {

    List<QuestionPool> findByDifficultyLevel(QuestionPool.DifficultyLevel level);
}
