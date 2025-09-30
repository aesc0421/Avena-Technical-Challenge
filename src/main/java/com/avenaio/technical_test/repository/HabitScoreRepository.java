package com.avenaio.technical_test.repository;

import com.avenaio.technical_test.model.HabitScore;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for managing habit score data
 */
@Repository
public interface HabitScoreRepository extends MongoRepository<HabitScore, String> {

    /**
     * Find habit score by user ID and date
     */
    Optional<HabitScore> findByUserIdAndDate(String userId, LocalDate date);

    /**
     * Find all habit scores for a specific user
     */
    List<HabitScore> findByUserIdOrderByDateDesc(String userId);

    /**
     * Find all habit scores for a specific date
     */
    List<HabitScore> findByDate(LocalDate date);

    /**
     * Find habit scores for a user within a date range
     */
    List<HabitScore> findByUserIdAndDateBetweenOrderByDateDesc(String userId, LocalDate startDate, LocalDate endDate);

    /**
     * Find habit score by habit record ID
     */
    Optional<HabitScore> findByHabitRecordId(String habitRecordId);

    /**
     * Check if a habit score exists for a user on a specific date
     */
    boolean existsByUserIdAndDate(String userId, LocalDate date);

    /**
     * Get the latest habit score for a user
     */
    @Query(value = "{'userId': ?0}", sort = "{'date': -1}")
    Optional<HabitScore> findLatestByUserId(String userId);

    /**
     * Get average overall score for a user over a period
     */
    @Query(value = "{'userId': ?0, 'date': {'$gte': ?1, '$lte': ?2}}")
    List<HabitScore> findByUserIdAndDateRange(String userId, LocalDate startDate, LocalDate endDate);

    /**
     * Delete habit scores older than a specific date
     */
    void deleteByDateBefore(LocalDate date);
}