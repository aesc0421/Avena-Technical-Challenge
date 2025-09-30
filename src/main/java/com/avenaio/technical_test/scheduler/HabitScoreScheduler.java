package com.avenaio.technical_test.scheduler;
import com.avenaio.technical_test.service.DailyHabitProcessingService;
import com.avenaio.technical_test.service.HabitQueueService;
import com.avenaio.technical_test.service.HabitRecordService;
import com.avenaio.technical_test.service.HabitSessionService;
import com.avenaio.technical_test.repository.UserRepository;
import com.avenaio.technical_test.model.HabitRecord;
import com.avenaio.technical_test.model.NutritionMeals;
import com.avenaio.technical_test.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Component
public class HabitScoreScheduler {

    private final DailyHabitProcessingService dailyHabitProcessingService;

    private static final Logger logger = LoggerFactory.getLogger(HabitScoreScheduler.class);

    @Autowired
    private HabitQueueService habitQueueService;
    
    @Autowired
    private HabitRecordService habitRecordService;
    
    
    @Autowired
    private HabitSessionService habitSessionService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Value("${app.mode:testing}")
    private String appMode;
    
    private final Random random = new Random();

    HabitScoreScheduler(DailyHabitProcessingService dailyHabitProcessingService) {
        this.dailyHabitProcessingService = dailyHabitProcessingService;
    }

    // Cron job for score calculation - PRODUCTION MODE: runs every minute
    @Scheduled(cron = "${spring.scheduling.cron.habit-score-scheduler}")
    public void calculateHabitScore() {
        // Silent execution - only call the first queue
        try {
            // Process TODAY's records for real-time scoring
            // For end-of-day processing, change to: LocalDate.now().minusDays(1)
            LocalDate targetDate = LocalDate.now();
            
            // Only enqueue the daily habit processing task (first queue)
            habitQueueService.enqueueDailyHabitProcessing(targetDate);
            
            // Silent completion

            // =================================================================================
            // TODO
            // The cron only calls the main queue.
            //  1. Start the first queue at 4:00 AM
            //  2. Get users dates records and enqueue into the second queue
            //  3. Calculate the score for each task in the second queue
            //  4. Add to the database the score calculated in the second queue
            //  5. Create the new time period for each user (inside the second queue)
 

            
        } catch (Exception e) {
            logger.error("Error occurred during habit score calculation", e);
        }
    }
    
    /**
     * Create new habit records for today if they don't exist
     */
//     private void createRecordsForToday() {
//         try {
//             LocalDate today = LocalDate.now();
//             logger.info("Creating habit records for today: {}", today);
            
//             List<User> users = userRepository.findAll();
//             int createdRecords = 0;
            
//             for (User user : users) {
//                 // Check if record already exists for today
//                 // if (habitRecordService.getHabitRecord(user, today).isPresent()) {
//                 //     logger.debug("Habit record already exists for user {} on {}", user.getId(), today);
//                 //     continue;
//                 // }
                
//                 // Create new habit record with random data
//                 HabitRecord newRecord = createRandomHabitRecord(user.getId(), today);
//                 habitRecordService.createOrUpdateHabitRecord(newRecord);
//                 createdRecords++;
                
//                 logger.info("Created new habit record for user {} on {}", user.getId(), today);
//             }
            
//             logger.info("Created {} new habit records for today: {}", createdRecords, today);
            
//         } catch (Exception e) {
//             logger.error("Error creating habit records for today", e);
//         }
//     }
    
//     /**
//      * Create new time periods for all users (simulates time lapse)
//      */
//     private void createNewTimePeriods(LocalDate targetDate) {
//         try {
//             List<User> users = userRepository.findAll();
//             int newPeriods = 0;
            
//             for (User user : users) {
//                 // Create new period for this user (time lapse)
//                 habitSessionService.createNewPeriod(user.getId(), targetDate, "Cron time lapse - minute boundary");
//                 newPeriods++;
//             }
            
//             if (newPeriods > 0) {
//                 logger.info("⏰ Created {} new time periods for date: {}", newPeriods, targetDate);
//             }
            
//         } catch (Exception e) {
//             logger.error("❌ Error creating new time periods for date: {}", targetDate, e);
//         }
//     }
    
//     /**
//      * Calculate scores for all users' current periods
//      * Creates score objects even if no data exists (indicating no information)
//      */
//     private void calculateScoresForAllCurrentPeriods(LocalDate targetDate) {
//         try {
//             List<User> users = userRepository.findAll();
//             int processedScores = 0;
            
//             for (User user : users) {
//                 // Force calculate score for current period (even if empty)
//                 var scorePeriod = habitSessionService.forceCalculateScoreForCurrentPeriod(user.getId(), targetDate);
                
//                 if (scorePeriod != null) {
//                     processedScores++;
                    
//                     // Log with indication if it's a "no data" score
//                     if (isEmptyPeriodScore(scorePeriod)) {
//                         logger.info("📊 No-data score created: 0% for user {} on {} (no habit information)", 
//                                    user.getId(), targetDate);
//                     } else {
//                         logger.info("✅ Period score calculated: {}% for user {} on {}", 
//                                    scorePeriod.getOverallScore(), user.getId(), targetDate);
//                     }
//                 }
//             }
            
//             if (processedScores > 0) {
//                 logger.info("📊 Total scores processed: {} for date: {}", processedScores, targetDate);
//             }
            
//         } catch (Exception e) {
//             logger.error("❌ Error calculating scores for date: {}", targetDate, e);
//         }
//     }
    
//     /**
//      * Check if a score period represents an empty period (no habit data)
//      */
//     private boolean isEmptyPeriodScore(com.avenaio.technical_test.model.ScorePeriod scorePeriod) {
//         return scorePeriod.getNutritionScore() == 0 && 
//                scorePeriod.getExerciseScore() == 0 && 
//                scorePeriod.getSleepScore() == 0 && 
//                scorePeriod.getHydrationScore() == 0;
//     }
    
//     /**
//      * Calculate and store scores for current session periods
//      */
//     private void calculateAndStoreSessionScores(LocalDate targetDate) {
//         try {
//             List<User> users = userRepository.findAll();
//             int processedScores = 0;
            
//             for (User user : users) {
//                 // Calculate score for current period
//                 var scorePeriod = habitSessionService.calculateScoreForCurrentPeriod(user.getId(), targetDate);
                
//                 if (scorePeriod != null) {
//                     processedScores++;
                    
//                     // Only log when a score is actually calculated
//                     logger.info("✅ Period score calculated: {}% for user {} on {}", 
//                                scorePeriod.getOverallScore(), user.getId(), targetDate);
//                 }
//             }
            
//             // Only log summary if scores were actually processed
//             if (processedScores > 0) {
//                 logger.info("📊 Total period scores calculated: {} for date: {}", processedScores, targetDate);
//             }
            
//         } catch (Exception e) {
//             logger.error("❌ Error calculating session scores for date: {}", targetDate, e);
//         }
//     }
    
//     /**
//      * Create fresh habit records for the next cycle (simulating a new day)
//      * This resets all habits as if it's a new day for users
//      */
//     private void createFreshHabitRecords() {
//         try {
//             LocalDate nextCycle = LocalDate.now();
//             logger.info("Creating fresh habit records for next cycle: {}", nextCycle);
            
//             List<User> users = userRepository.findAll();
//             int updatedRecords = 0;
            
//             for (User user : users) {
//                 // Check if record already exists for this user and date
//                 Optional<HabitRecord> existingRecord = habitRecordService.getHabitRecord(user, nextCycle);
                
//                 if (existingRecord.isPresent()) {
//                     // Update existing record with fresh state
//                     HabitRecord record = existingRecord.get();
//                     resetHabitRecord(record);
//                     habitRecordService.createOrUpdateHabitRecord(record);
//                     updatedRecords++;
                    
//                     logger.info("Updated existing habit record to fresh state for user {} for next cycle", user.getId());
//                 } else {
//                     // Create a new habit record with fresh state
//                     HabitRecord freshRecord = createFreshHabitRecord(user.getId(), nextCycle);
//                     habitRecordService.createOrUpdateHabitRecord(freshRecord);
//                     updatedRecords++;
                    
//                     logger.info("Created fresh habit record for user {} for next cycle", user.getId());
//                 }
//             }
            
//             logger.info("Processed {} habit records for next cycle: {}", updatedRecords, nextCycle);
            
//         } catch (Exception e) {
//             logger.error("Error creating fresh habit records for next cycle", e);
//         }
//     }
    
//     /**
//      * Create a random habit record for testing
//      */
//     private HabitRecord createRandomHabitRecord(String userId, LocalDate date) {
//         HabitRecord record = new HabitRecord();
//         record.setUserId(userId);
//         record.setDate(date);
        
//         // Create random nutrition meals
//         NutritionMeals nutritionMeals = new NutritionMeals();
//         nutritionMeals.setBreakfast(random.nextBoolean());
//         nutritionMeals.setSnackOne(random.nextBoolean());
//         nutritionMeals.setMeal(random.nextBoolean());
//         nutritionMeals.setSnackTwo(random.nextBoolean());
//         nutritionMeals.setDinner(random.nextBoolean());
//         record.setNutritionMeals(nutritionMeals);
        
//         // Random exercise minutes (0-120)
//         record.setExerciseMinutes(random.nextInt(121));
        
//         // Random sleep minutes (300-600 minutes = 5-10 hours)
//         record.setSleepMinutes(300 + random.nextInt(301));
        
//         // Random hydration (500-3000 ml)
//         record.setHydrationMl(500 + random.nextInt(2501));
        
//         record.setNotes("Auto-generated record for testing at " + java.time.LocalDateTime.now());
        
//         return record;
//     }
    
//     /**
//      * Create a fresh habit record with reset state (simulating new day)
//      */
//     private HabitRecord createFreshHabitRecord(String userId, LocalDate date) {
//         HabitRecord record = new HabitRecord();
//         record.setUserId(userId);
//         record.setDate(date);
        
//         // Create fresh nutrition meals (all false - new day, no meals eaten yet)
//         NutritionMeals nutritionMeals = new NutritionMeals();
//         nutritionMeals.setBreakfast(false);
//         nutritionMeals.setSnackOne(false);
//         nutritionMeals.setMeal(false);
//         nutritionMeals.setSnackTwo(false);
//         nutritionMeals.setDinner(false);
//         record.setNutritionMeals(nutritionMeals);
        
//         // Reset all habits to starting values
//         record.setExerciseMinutes(0);
//         record.setSleepMinutes(0);
//         record.setHydrationMl(0);
        
//         record.setNotes("Fresh habit record created for new day cycle at " + java.time.LocalDateTime.now());
        
//         return record;
//     }
    
//     /**
//      * Reset an existing habit record to fresh state
//      */
//     private void resetHabitRecord(HabitRecord record) {
//         // Reset nutrition meals (all false - new day, no meals eaten yet)
//         NutritionMeals nutritionMeals = new NutritionMeals();
//         nutritionMeals.setBreakfast(false);
//         nutritionMeals.setSnackOne(false);
//         nutritionMeals.setMeal(false);
//         nutritionMeals.setSnackTwo(false);
//         nutritionMeals.setDinner(false);
//         record.setNutritionMeals(nutritionMeals);
        
//         // Reset all habits to starting values
//         record.setExerciseMinutes(0);
//         record.setSleepMinutes(0);
//         record.setHydrationMl(0);
        
//         // Clear the overall score so it can be recalculated
//         record.setOverallScore(null);
        
//         record.setNotes("Habit record reset for new day cycle at " + java.time.LocalDateTime.now());
//     }
// }
}