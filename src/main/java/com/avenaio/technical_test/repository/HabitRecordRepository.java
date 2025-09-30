package com.avenaio.technical_test.repository;

import com.avenaio.technical_test.model.HabitRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for HabitRecord entity
 */
@Repository
public interface HabitRecordRepository extends MongoRepository<HabitRecord, String> {

    Optional<HabitRecord> findByUserIdAndDate(String userId, LocalDate date);

    List<HabitRecord> findByDate(LocalDate date);

    List<HabitRecord> findByUserIdOrderByDateDesc(String userId);

    List<HabitRecord> findByUserIdAndDateBetween(String userId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Find habit records by user ID and date, ordered by creation date descending
     */
    List<HabitRecord> findByUserIdAndDateOrderByCreatedAtDesc(String userId, LocalDate date);

    @Query(value = "{'userId': ?0}", fields = "{'overallScore': 1}")
    List<HabitRecord> findOverallScoresByUserId(String userId);

    @Query(value = "{'userId': ?0, 'nutritionRating': {$ne: null}}", fields = "{'nutritionRating': 1}")
    List<HabitRecord> findNutritionRatingsByUserId(String userId);

    @Query(value = "{'userId': ?0, 'exerciseRating': {$ne: null}}", fields = "{'exerciseRating': 1}")
    List<HabitRecord> findExerciseRatingsByUserId(String userId);

    @Query(value = "{'userId': ?0, 'sleepRating': {$ne: null}}", fields = "{'sleepRating': 1}")
    List<HabitRecord> findSleepRatingsByUserId(String userId);

    @Query(value = "{'userId': ?0, 'hydrationRating': {$ne: null}}", fields = "{'hydrationRating': 1}")
    List<HabitRecord> findHydrationRatingsByUserId(String userId);
}