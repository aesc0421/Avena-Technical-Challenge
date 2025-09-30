package com.avenaio.technical_test.service;

import com.avenaio.technical_test.model.*;
import com.avenaio.technical_test.repository.HabitSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing habit sessions with time-based periods
 */
@Service
public class HabitSessionService {

    // private static final Logger logger = LoggerFactory.getLogger(HabitSessionService.class);

    // @Autowired
    // private HabitSessionRepository habitSessionRepository;

    // @Autowired
    // private HabitScoreCalculator habitScoreCalculator;

    // /**
    //  * Get or create a habit session for a user on a specific date
    //  */
    // public HabitSession getOrCreateSession(String userId, LocalDate date) {
    //     Optional<HabitSession> existingSession = habitSessionRepository.findByUserIdAndDate(userId, date);
        
    //     if (existingSession.isPresent()) {
    //         return existingSession.get();
    //     }
        
    //     // Create new session
    //     HabitSession newSession = new HabitSession();
    //     newSession.setUserId(userId);
    //     newSession.setDate(date);
    //     newSession.setSessionStartTime(LocalDateTime.now());
    //     newSession.setLastActivityTime(LocalDateTime.now());
        
    //     // Create the first habit period
    //     String firstPeriodId = UUID.randomUUID().toString();
    //     HabitPeriod firstPeriod = new HabitPeriod(firstPeriodId, LocalDateTime.now());
    //     newSession.addNewHabitPeriod(firstPeriod);
        
    //     return habitSessionRepository.save(newSession);
    // }

    // /**
    //  * Create a new habit period when time lapses
    //  */
    // public HabitSession createNewPeriod(String userId, LocalDate date, String reason) {
    //     HabitSession session = getOrCreateSession(userId, date);
        
    //     // End the current period if it exists and is active
    //     HabitPeriod currentPeriod = session.getCurrentHabitPeriod();
    //     if (currentPeriod != null && currentPeriod.isActive()) {
    //         currentPeriod.endPeriod();
    //     }
        
    //     // Create new period
    //     String newPeriodId = UUID.randomUUID().toString();
    //     HabitPeriod newPeriod = new HabitPeriod(newPeriodId, LocalDateTime.now());
    //     newPeriod.setNotes("New period created: " + reason);
        
    //     session.addNewHabitPeriod(newPeriod);
        
    //     logger.info("✨ New habit period created for user {} on {} - Reason: {}", userId, date, reason);
        
    //     return habitSessionRepository.save(session);
    // }

    // /**
    //  * Update a meal in the current active period
    //  */
    // public HabitSession updateMealInCurrentPeriod(String userId, LocalDate date, String mealType, boolean consumed) {
    //     HabitSession session = getOrCreateSession(userId, date);
    //     HabitPeriod currentPeriod = session.getCurrentHabitPeriod();
        
    //     if (currentPeriod == null) {
    //         // Create first period if none exists
    //         String periodId = UUID.randomUUID().toString();
    //         currentPeriod = new HabitPeriod(periodId, LocalDateTime.now());
    //         session.addNewHabitPeriod(currentPeriod);
    //     }
        
    //     // Initialize nutrition meals if null
    //     if (currentPeriod.getNutritionMeals() == null) {
    //         currentPeriod.setNutritionMeals(new NutritionMeals());
    //     }
        
    //     // Update the specific meal
    //     switch (mealType.toLowerCase()) {
    //         case "breakfast":
    //             currentPeriod.getNutritionMeals().setBreakfast(consumed);
    //             break;
    //         case "snack_one":
    //             currentPeriod.getNutritionMeals().setSnackOne(consumed);
    //             break;
    //         case "meal":
    //             currentPeriod.getNutritionMeals().setMeal(consumed);
    //             break;
    //         case "snack_two":
    //             currentPeriod.getNutritionMeals().setSnackTwo(consumed);
    //             break;
    //         case "dinner":
    //             currentPeriod.getNutritionMeals().setDinner(consumed);
    //             break;
    //         default:
    //             throw new IllegalArgumentException("Invalid meal type: " + mealType);
    //     }
        
    //     currentPeriod.touch(); // Update last modified time
    //     session.setLastActivityTime(LocalDateTime.now());
        
    //     return habitSessionRepository.save(session);
    // }

    // /**
    //  * Update exercise in the current active period
    //  */
    // public HabitSession updateExerciseInCurrentPeriod(String userId, LocalDate date, Integer minutes) {
    //     HabitSession session = getOrCreateSession(userId, date);
    //     HabitPeriod currentPeriod = session.getCurrentHabitPeriod();
        
    //     if (currentPeriod != null) {
    //         currentPeriod.setExerciseMinutes(minutes);
    //         currentPeriod.touch();
    //         session.setLastActivityTime(LocalDateTime.now());
    //     }
        
    //     return habitSessionRepository.save(session);
    // }

    // /**
    //  * Update sleep in the current active period
    //  */
    // public HabitSession updateSleepInCurrentPeriod(String userId, LocalDate date, Integer minutes) {
    //     HabitSession session = getOrCreateSession(userId, date);
    //     HabitPeriod currentPeriod = session.getCurrentHabitPeriod();
        
    //     if (currentPeriod != null) {
    //         currentPeriod.setSleepMinutes(minutes);
    //         currentPeriod.touch();
    //         session.setLastActivityTime(LocalDateTime.now());
    //     }
        
    //     return habitSessionRepository.save(session);
    // }

    // /**
    //  * Update hydration in the current active period
    //  */
    // public HabitSession updateHydrationInCurrentPeriod(String userId, LocalDate date, Integer ml) {
    //     HabitSession session = getOrCreateSession(userId, date);
    //     HabitPeriod currentPeriod = session.getCurrentHabitPeriod();
        
    //     if (currentPeriod != null) {
    //         currentPeriod.setHydrationMl(ml);
    //         currentPeriod.touch();
    //         session.setLastActivityTime(LocalDateTime.now());
    //     }
        
    //     return habitSessionRepository.save(session);
    // }

    // /**
    //  * Calculate and store score for the current period
    //  */
    // public ScorePeriod calculateScoreForCurrentPeriod(String userId, LocalDate date) {
    //     HabitSession session = getOrCreateSession(userId, date);
    //     HabitPeriod currentPeriod = session.getCurrentHabitPeriod();
        
    //     if (currentPeriod == null) {
    //         return null;
    //     }
        
    //     // Check if this period already has a score
    //     boolean hasScore = session.getScorePeriods().stream()
    //         .anyMatch(sp -> sp.getPeriodId().equals(currentPeriod.getPeriodId()));
        
    //     if (hasScore) {
    //         // Return existing score instead of recalculating
    //         return session.getScorePeriods().stream()
    //             .filter(sp -> sp.getPeriodId().equals(currentPeriod.getPeriodId()))
    //             .findFirst().orElse(null);
    //     }
        
    //     // Create a temporary HabitRecord for score calculation
    //     HabitRecord tempRecord = new HabitRecord();
    //     tempRecord.setNutritionMeals(currentPeriod.getNutritionMeals());
    //     tempRecord.setExerciseMinutes(currentPeriod.getExerciseMinutes());
    //     tempRecord.setSleepMinutes(currentPeriod.getSleepMinutes());
    //     tempRecord.setHydrationMl(currentPeriod.getHydrationMl());
        
    //     // Calculate scores
    //     int nutritionScore = habitScoreCalculator.calculateNutritionScore(tempRecord);
    //     int exerciseScore = habitScoreCalculator.calculateExerciseScore(tempRecord);
    //     int sleepScore = habitScoreCalculator.calculateSleepScore(tempRecord);
    //     int hydrationScore = habitScoreCalculator.calculateHydrationScore(tempRecord);
        
    //     // Calculate overall score
    //     int totalScore = nutritionScore + exerciseScore + sleepScore + hydrationScore;
    //     BigDecimal overallScore = BigDecimal.valueOf(totalScore)
    //             .divide(BigDecimal.valueOf(4), 2, java.math.RoundingMode.HALF_UP);
        
    //     // Create score period
    //     String scoreId = UUID.randomUUID().toString();
    //     ScorePeriod scorePeriod = new ScorePeriod(
    //             scoreId, currentPeriod.getPeriodId(),
    //             nutritionScore, exerciseScore, sleepScore, hydrationScore,
    //             overallScore
    //     );
        
    //     // Add to session
    //     session.addScorePeriod(scorePeriod);
    //     habitSessionRepository.save(session);
        
    //     logger.info("✅ Score calculated for period {}: {}% for user {} on {}", 
    //                currentPeriod.getPeriodId(), overallScore, userId, date);
        
    //     return scorePeriod;
    // }

    // /**
    //  * Force calculate score for current period (even if no data exists)
    //  * This ensures every period gets a corresponding score object
    //  */
    // public ScorePeriod forceCalculateScoreForCurrentPeriod(String userId, LocalDate date) {
    //     HabitSession session = getOrCreateSession(userId, date);
    //     HabitPeriod currentPeriod = session.getCurrentHabitPeriod();
        
    //     if (currentPeriod == null) {
    //         return null;
    //     }
        
    //     // Check if this period already has a score
    //     boolean hasScore = session.getScorePeriods().stream()
    //         .anyMatch(sp -> sp.getPeriodId().equals(currentPeriod.getPeriodId()));
        
    //     if (hasScore) {
    //         // Return existing score instead of recalculating
    //         return session.getScorePeriods().stream()
    //             .filter(sp -> sp.getPeriodId().equals(currentPeriod.getPeriodId()))
    //             .findFirst().orElse(null);
    //     }
        
    //     // Create a temporary HabitRecord for score calculation
    //     HabitRecord tempRecord = new HabitRecord();
    //     tempRecord.setNutritionMeals(currentPeriod.getNutritionMeals() != null ? 
    //                                 currentPeriod.getNutritionMeals() : new NutritionMeals());
    //     tempRecord.setExerciseMinutes(currentPeriod.getExerciseMinutes() != null ? 
    //                                 currentPeriod.getExerciseMinutes() : 0);
    //     tempRecord.setSleepMinutes(currentPeriod.getSleepMinutes() != null ? 
    //                              currentPeriod.getSleepMinutes() : 0);
    //     tempRecord.setHydrationMl(currentPeriod.getHydrationMl() != null ? 
    //                              currentPeriod.getHydrationMl() : 0);
        
    //     // Calculate scores (will be 0 if no data)
    //     int nutritionScore = habitScoreCalculator.calculateNutritionScore(tempRecord);
    //     int exerciseScore = habitScoreCalculator.calculateExerciseScore(tempRecord);
    //     int sleepScore = habitScoreCalculator.calculateSleepScore(tempRecord);
    //     int hydrationScore = habitScoreCalculator.calculateHydrationScore(tempRecord);
        
    //     // Calculate overall score
    //     int totalScore = nutritionScore + exerciseScore + sleepScore + hydrationScore;
    //     BigDecimal overallScore = BigDecimal.valueOf(totalScore)
    //             .divide(BigDecimal.valueOf(4), 2, java.math.RoundingMode.HALF_UP);
        
    //     // Create score period (will automatically detect if it's a no-data score)
    //     String scoreId = UUID.randomUUID().toString();
    //     ScorePeriod scorePeriod = new ScorePeriod(
    //             scoreId, currentPeriod.getPeriodId(),
    //             nutritionScore, exerciseScore, sleepScore, hydrationScore,
    //             overallScore
    //     );
        
    //     // Add to session
    //     session.addScorePeriod(scorePeriod);
    //     habitSessionRepository.save(session);
        
    //     // Log based on data status
    //     if (scorePeriod.isHasData()) {
    //         logger.info("✅ Score calculated for current period {}: {}% for user {} on {}", 
    //                    currentPeriod.getPeriodId(), overallScore, userId, date);
    //     } else {
    //         logger.info("📊 No-data score created for period {}: 0% for user {} on {} (no habit information)", 
    //                    currentPeriod.getPeriodId(), userId, date);
    //     }
        
    //     return scorePeriod;
    // }

    // /**
    //  * Calculate score for a specific period (used when ending periods)
    //  */
    // private ScorePeriod calculateScoreForSpecificPeriod(HabitSession session, HabitPeriod habitPeriod) {
    //     // Check if this period already has a score
    //     boolean hasScore = session.getScorePeriods().stream()
    //         .anyMatch(sp -> sp.getPeriodId().equals(habitPeriod.getPeriodId()));
        
    //     if (hasScore) {
    //         return session.getScorePeriods().stream()
    //             .filter(sp -> sp.getPeriodId().equals(habitPeriod.getPeriodId()))
    //             .findFirst().orElse(null);
    //     }
        
    //     // Create a temporary HabitRecord for score calculation
    //     HabitRecord tempRecord = new HabitRecord();
    //     tempRecord.setNutritionMeals(habitPeriod.getNutritionMeals());
    //     tempRecord.setExerciseMinutes(habitPeriod.getExerciseMinutes());
    //     tempRecord.setSleepMinutes(habitPeriod.getSleepMinutes());
    //     tempRecord.setHydrationMl(habitPeriod.getHydrationMl());
        
    //     // Calculate scores
    //     int nutritionScore = habitScoreCalculator.calculateNutritionScore(tempRecord);
    //     int exerciseScore = habitScoreCalculator.calculateExerciseScore(tempRecord);
    //     int sleepScore = habitScoreCalculator.calculateSleepScore(tempRecord);
    //     int hydrationScore = habitScoreCalculator.calculateHydrationScore(tempRecord);
        
    //     // Calculate overall score
    //     int totalScore = nutritionScore + exerciseScore + sleepScore + hydrationScore;
    //     BigDecimal overallScore = BigDecimal.valueOf(totalScore)
    //             .divide(BigDecimal.valueOf(4), 2, java.math.RoundingMode.HALF_UP);
        
    //     // Create score period
    //     String scoreId = UUID.randomUUID().toString();
    //     ScorePeriod scorePeriod = new ScorePeriod(
    //             scoreId, habitPeriod.getPeriodId(),
    //             nutritionScore, exerciseScore, sleepScore, hydrationScore,
    //             overallScore
    //     );
        
    //     // Add to session
    //     session.addScorePeriod(scorePeriod);
        
    //     logger.info("✅ Score calculated for ending period {}: {}% for user {} on {}", 
    //                habitPeriod.getPeriodId(), overallScore, session.getUserId(), session.getDate());
        
    //     return scorePeriod;
    // }

    // /**
    //  * Get all sessions for a user
    //  */
    // public List<HabitSession> getUserSessions(String userId) {
    //     return habitSessionRepository.findByUserIdOrderByDateDesc(userId);
    // }

    // /**
    //  * Get session for a specific user and date
    //  */
    // public Optional<HabitSession> getSession(String userId, LocalDate date) {
    //     return habitSessionRepository.findByUserIdAndDate(userId, date);
    // }

    // /**
    //  * Get all sessions for a specific date
    //  */
    // public List<HabitSession> getSessionsByDate(LocalDate date) {
    //     return habitSessionRepository.findByDate(date);
    // }
}