package com.globetrotter.repository;

import com.globetrotter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUsername(String username);
    
    @Modifying
    @Query("UPDATE User u SET u.score = u.score + ?2 WHERE u.id = ?1")
    void updateScore(Long userId, Integer points);
} 