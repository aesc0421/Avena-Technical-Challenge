package com.avenaio.technical_test.service;

import com.avenaio.technical_test.dto.HabitScoreTaskMessage;
import com.avenaio.technical_test.model.HabitRecord;
import com.avenaio.technical_test.model.HabitScore;
import com.avenaio.technical_test.model.HabitSession;
import com.avenaio.technical_test.model.HabitDate;
import com.avenaio.technical_test.model.HabitPeriod;
import com.avenaio.technical_test.model.NutritionMeals;
import com.avenaio.technical_test.repository.HabitDateRepository;
import com.avenaio.technical_test.repository.HabitRecordRepository;
import com.avenaio.technical_test.repository.HabitSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

/**
 * Service for processing individual habit score calculations
 */
@Service
public class HabitScoreCalculationService {

    private static final Logger logger = LoggerFactory.getLogger(HabitScoreCalculationService.class);
    private static final int MAX_RETRY_ATTEMPTS = 3;

    @Autowired
    private HabitRecordRepository habitRecordRepository;
    
    @Autowired
    private HabitSessionRepository habitSessionRepository;

    @Autowired
    private HabitDateService habitDateService;
    
    @Autowired
    private HabitScoreService habitScoreService;
    
    private final Random random = new Random();

    /**
     * Process individual habit score calculation task
     */
    @RabbitListener(queues = "${habit.queue.individual}")
    public void processHabitScoreCalculation(HabitScoreTaskMessage message) {
        logger.info("🔄 Second queue received message - Processing habit record: {} for user: {} with taskId: {}", 
            message.getHabitRecordId(), message.getUserId(), message.getTaskId());
        
        try {
            // Find the habit session (message.getHabitRecordId() now contains session ID)
            HabitDate habitDate = habitDateService.getHabitById(message.getHabitRecordId());
            
            if (habitDate == null) {
                logger.warn("Habit session not found: {} with taskId: {}", 
                    message.getHabitRecordId(), message.getTaskId());
                return;
            }
            
            // Check if score is already calculated in the session
            // if (!habitDate.getScorePeriods().isEmpty()) {
            //     logger.info("Session {} already has {} score periods calculated", 
            //         session.getId(), session.getScorePeriods().size());
            //     return; // Scores already exist
            // }
            
            // Get the current habit period to calculate score from
            // if (session.getHabitPeriods().isEmpty()) {
            //     logger.warn("No habit periods found in session: {}", session.getId());
            //     return;
            // }
            
            // Get the current period (or the last period if session is completed)
            // HabitPeriod currentPeriod = session.getCurrentHabitPeriod();
            // if (currentPeriod == null && !session.getHabitPeriods().isEmpty()) {
            //     currentPeriod = session.getHabitPeriods().get(session.getHabitPeriods().size() - 1);
            // }
            
            // if (currentPeriod == null) {
            //     logger.warn("No valid habit period found in session: {}", session.getId());
            //     return;
            // }
            
            // Create a temporary HabitRecord from the HabitPeriod for score calculation
            // HabitRecord tempRecord = new HabitRecord();
            // tempRecord.setUserId(session.getUserId());
            // tempRecord.setDate(session.getDate());
            // tempRecord.setNutritionMeals(currentPeriod.getNutritionMeals());
            // tempRecord.setExerciseMinutes(currentPeriod.getExerciseMinutes());
            // tempRecord.setSleepMinutes(currentPeriod.getSleepMinutes());
            // tempRecord.setHydrationMl(currentPeriod.getHydrationMl());


            
            // Calculate and save the score to the scores collection
            HabitDate.Score habitScore = habitScoreService.calculateAndSaveScore(habitDate);
            // Get dayScore
            double totalScore = habitScore.getNutrition() + habitScore.getExercise() + habitScore.getSleep() + habitScore.getHydration();
            double dayScore = totalScore / 4;

            habitDate.setScore(habitScore);
            habitDate.setDayScore(dayScore);
            
            habitDateService.updateHabitDate(habitDate);
            
            logger.info("✅ Queue processed - Habit score: {}% for user {} on {} from session: {}", 
                habitDate.getDayScore(), habitDate.getPatientId(), habitDate.getRegistrationDate(), habitDate.getId());
        } catch (Exception e) {
            logger.error("Error processing habit score calculation for record: {} with taskId: {}", 
                message.getHabitRecordId(), message.getTaskId(), e);
            
            // Handle retry logic
            handleRetry(message, e);
        }
    }

    /**
     * Handle retry logic for failed tasks
     */
    private void handleRetry(HabitScoreTaskMessage message, Exception error) {
        if (message.getRetryCount() < MAX_RETRY_ATTEMPTS) {
            message.setRetryCount(message.getRetryCount() + 1);
            logger.warn("Retrying habit score calculation for record: {} (attempt {}/{}) with taskId: {}", 
                message.getHabitRecordId(), message.getRetryCount(), MAX_RETRY_ATTEMPTS, message.getTaskId());
            
            // Re-queue the message with increased retry count
            // Note: In a production environment, you might want to add a delay before retrying
            try {
                // For now, we'll just log the retry attempt
                // In a real implementation, you would re-queue the message here
                logger.info("Would re-queue message for retry: {}", message);
            } catch (Exception retryError) {
                logger.error("Failed to re-queue message for retry: {}", message, retryError);
            }
        } else {
            logger.error("Max retry attempts reached for habit score calculation. Record: {} with taskId: {}", 
                message.getHabitRecordId(), message.getTaskId());
            
            // In a production environment, you might want to:
            // 1. Send to a dead letter queue
            // 2. Send an alert to administrators
            // 3. Log to a monitoring system
        }
    }
    
    /**
     * Create a new habit record for the next day (for continuous testing)
     */
    private void createNewHabitRecordForNextDay(String userId) {
        try {
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            
            // Check if record already exists for tomorrow
            if (habitRecordRepository.findByUserIdAndDate(userId, tomorrow).isPresent()) {
                logger.debug("Habit record already exists for user {} on {}", userId, tomorrow);
                return;
            }
            
            // Create new habit record with random data
            HabitRecord newRecord = createRandomHabitRecord(userId, tomorrow);
            habitRecordRepository.save(newRecord);
            
            logger.info("Created new habit record for user {} on {} for continuous testing", userId, tomorrow);
            
        } catch (Exception e) {
            logger.error("Error creating new habit record for user {}: {}", userId, e.getMessage());
        }
    }
    
    /**
     * Create a random habit record for testing
     */
    private HabitRecord createRandomHabitRecord(String userId, LocalDate date) {
        HabitRecord record = new HabitRecord();
        record.setUserId(userId);
        record.setDate(date);
        
        // Create random nutrition meals
        NutritionMeals nutritionMeals = new NutritionMeals();
        nutritionMeals.setBreakfast(random.nextBoolean());
        nutritionMeals.setSnackOne(random.nextBoolean());
        nutritionMeals.setMeal(random.nextBoolean());
        nutritionMeals.setSnackTwo(random.nextBoolean());
        nutritionMeals.setDinner(random.nextBoolean());
        record.setNutritionMeals(nutritionMeals);
        
        // Random exercise minutes (0-120)
        record.setExerciseMinutes(random.nextInt(121));
        
        // Random sleep minutes (300-600 minutes = 5-10 hours)
        record.setSleepMinutes(300 + random.nextInt(301));
        
        // Random hydration (500-3000 ml)
        record.setHydrationMl(500 + random.nextInt(2501));
        
        record.setNotes("Auto-generated record for continuous testing at " + java.time.LocalDateTime.now());
        
        return record;
    }
}