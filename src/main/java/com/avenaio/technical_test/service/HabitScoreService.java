package com.avenaio.technical_test.service;

import com.avenaio.technical_test.model.HabitDate;
import com.avenaio.technical_test.model.HabitRecord;
import com.avenaio.technical_test.model.HabitScore;
import com.avenaio.technical_test.model.HabitDate.Score;
import com.avenaio.technical_test.repository.HabitScoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing habit scores
 */
@Service
public class HabitScoreService {

    private static final Logger logger = LoggerFactory.getLogger(HabitScoreService.class);

    @Autowired
    private HabitScoreRepository habitScoreRepository;

    @Autowired
    private HabitScoreCalculator habitScoreCalculator;

    /**
     * Calculate and save habit score from a habit record
     */
    public Score calculateAndSaveScore(HabitDate habitDate) {
        // Silent calculation

        try {
            // Check if score already exists
            // Optional<HabitScore> existingScore = habitScoreRepository.findByHabitRecordId(habitDate.getId());
            // if (existingScore.isPresent()) {
            //     return existingScore.get();
            // }

            // Calculate individual scores
            int nutritionScore = habitScoreCalculator.calculateNutritionScore(habitDate);
            int exerciseScore = habitScoreCalculator.calculateExerciseScore(habitDate);
            int sleepScore = habitScoreCalculator.calculateSleepScore(habitDate);
            int hydrationScore = habitScoreCalculator.calculateHydrationScore(habitDate);

            // Calculate overall score
            // int totalScore = nutritionScore + exerciseScore + sleepScore + hydrationScore;
            // BigDecimal overallScore = BigDecimal.valueOf(totalScore)
            //         .divide(BigDecimal.valueOf(4), 2, java.math.RoundingMode.HALF_UP);
            // double overallScore = totalScore / 4;

            // Create and save habit score
            HabitDate.Score habitScore = new HabitDate.Score(
                    nutritionScore,
                    exerciseScore,
                    sleepScore,
                    hydrationScore
            );
            
            // Score saved silently - logging handled by scheduler

            return habitScore;

        } catch (Exception e) {
            logger.error("Error calculating and saving score for habit record: {}", habitDate.getId(), e);
            throw new RuntimeException("Failed to calculate and save habit score", e);
        }
    }

    /**
     * Get habit score by user ID and date
     */
    public Optional<HabitScore> getHabitScore(String userId, LocalDate date) {
        return habitScoreRepository.findByUserIdAndDate(userId, date);
    }

    /**
     * Get all habit scores for a user
     */
    public List<HabitScore> getUserHabitScores(String userId) {
        return habitScoreRepository.findByUserIdOrderByDateDesc(userId);
    }

    /**
     * Get habit scores for a user within a date range
     */
    public List<HabitScore> getUserHabitScores(String userId, LocalDate startDate, LocalDate endDate) {
        return habitScoreRepository.findByUserIdAndDateBetweenOrderByDateDesc(userId, startDate, endDate);
    }

    /**
     * Get all habit scores for a specific date
     */
    public List<HabitScore> getHabitScoresByDate(LocalDate date) {
        return habitScoreRepository.findByDate(date);
    }

    /**
     * Check if a habit score exists for a user on a specific date
     */
    public boolean hasHabitScore(String userId, LocalDate date) {
        return habitScoreRepository.existsByUserIdAndDate(userId, date);
    }

    /**
     * Get the latest habit score for a user
     */
    public Optional<HabitScore> getLatestHabitScore(String userId) {
        return habitScoreRepository.findLatestByUserId(userId);
    }

    /**
     * Calculate average score for a user over a period
     */
    public BigDecimal calculateAverageScore(String userId, LocalDate startDate, LocalDate endDate) {
        List<HabitScore> scores = habitScoreRepository.findByUserIdAndDateRange(userId, startDate, endDate);
        
        if (scores.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = scores.stream()
                .map(HabitScore::getOverallScore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.divide(BigDecimal.valueOf(scores.size()), 2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Delete old habit scores (cleanup utility)
     */
    public void deleteOldScores(LocalDate beforeDate) {
        logger.info("Deleting habit scores older than: {}", beforeDate);
        habitScoreRepository.deleteByDateBefore(beforeDate);
    }
}