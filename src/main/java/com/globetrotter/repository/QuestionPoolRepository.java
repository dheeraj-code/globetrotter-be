package com.globetrotter.repository;

import com.globetrotter.model.City;
import com.globetrotter.model.QuestionPool;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface QuestionPoolRepository extends JpaRepository {
    List<QuestionPool> findByLastUsedBefore(LocalDateTime dateTime);
    List<QuestionPool> findByTimesUsed(Integer maxUse);
    boolean existsByCityAndClue(City city, String clue);
}
