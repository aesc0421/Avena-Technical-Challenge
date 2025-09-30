package com.avenaio.technical_test.repository;

import com.avenaio.technical_test.model.HabitSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for managing habit session data
 */
@Repository
public interface HabitSessionRepository extends MongoRepository<HabitSession, String> {

    /**
     * Find habit session by user ID and date
     */
    Optional<HabitSession> findByUserIdAndDate(String userId, LocalDate date);

    /**
     * Find all habit sessions for a specific user
     */
    List<HabitSession> findByUserIdOrderByDateDesc(String userId);

    /**
     * Find all habit sessions for a specific date
     */
    List<HabitSession> findByDate(LocalDate date);

    /**
     * Find habit sessions for a user within a date range
     */
    List<HabitSession> findByUserIdAndDateBetweenOrderByDateDesc(String userId, LocalDate startDate, LocalDate endDate);

    /**
     * Check if a habit session exists for a user on a specific date
     */
    boolean existsByUserIdAndDate(String userId, LocalDate date);

    /**
     * Get the latest habit session for a user
     */
    @Query(value = "{'userId': ?0}", sort = "{'date': -1}")
    Optional<HabitSession> findLatestByUserId(String userId);

    /**
     * Find sessions with active periods
     */
    @Query("{'habitPeriods.isActive': true}")
    List<HabitSession> findSessionsWithActivePeriods();

    /**
     * Delete habit sessions older than a specific date
     */
    void deleteByDateBefore(LocalDate date);
}