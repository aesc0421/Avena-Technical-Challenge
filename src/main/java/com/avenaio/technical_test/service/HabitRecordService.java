package com.avenaio.technical_test.service;

import com.avenaio.technical_test.model.HabitRecord;
import com.avenaio.technical_test.model.User;
import com.avenaio.technical_test.repository.HabitRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class HabitRecordService {
    
    // @Autowired
    // private HabitRecordRepository habitRecordRepository;
    
    // @Autowired
    // private HabitScoreCalculator habitScoreCalculator;

    // /**
    //  * Saves a habit record
    //  * @param habitRecord The habit record to save
    //  * @return The saved habit record
    //  */
    // public HabitRecord saveHabitRecord(HabitRecord habitRecord) {
    //     return habitRecordRepository.save(habitRecord);
    // }


    // /**
    //  * Updates nutrition meal count (0-5 meals)
    //  * @param user The user
    //  * @param date The date
    //  * @param mealsCount Number of meals consumed (0-5)
    //  * @return The updated habit record
    //  */
    // public HabitRecord updateMeals(User user, LocalDate date, Integer mealsCount) {
    //     Optional<HabitRecord> existingRecord = habitRecordRepository.findByUserIdAndDate(user.getId(), date);
        
    //     HabitRecord record;
    //     if (existingRecord.isPresent()) {
    //         record = existingRecord.get();
    //     } else {
    //         record = new HabitRecord();
    //         record.setUserId(user.getId());
    //         record.setDate(date);
    //     }
        
    //     // Validate meal count (0-5)
    //     if (mealsCount < 0 || mealsCount > 5) {
    //         throw new IllegalArgumentException("Meal count must be between 0 and 5");
    //     }
        
    //     record.setMealsCount(mealsCount);
        
    //     return habitRecordRepository.save(record);
    // }

    // /**
    //  * Updates exercise minutes
    //  * @param user The user
    //  * @param date The date
    //  * @param minutes Minutes of exercise
    //  * @return The updated habit record
    //  */
    // public HabitRecord updateExercise(User user, LocalDate date, Integer minutes) {
    //     Optional<HabitRecord> existingRecord = habitRecordRepository.findByUserIdAndDate(user.getId(), date);
        
    //     HabitRecord record;
    //     if (existingRecord.isPresent()) {
    //         record = existingRecord.get();
    //     } else {
    //         record = new HabitRecord();
    //         record.setUserId(user.getId());
    //         record.setDate(date);
    //     }
        
    //     record.setExerciseMinutes(minutes);
    //     record.setOverallScore(null); // Clear score - will be recalculated by cron job
        
    //     return habitRecordRepository.save(record);
    // }

    // /**
    //  * Updates sleep minutes
    //  * @param user The user
    //  * @param date The date
    //  * @param minutes Minutes of sleep
    //  * @return The updated habit record
    //  */
    // public HabitRecord updateSleep(User user, LocalDate date, Integer minutes) {
    //     Optional<HabitRecord> existingRecord = habitRecordRepository.findByUserIdAndDate(user.getId(), date);
        
    //     HabitRecord record;
    //     if (existingRecord.isPresent()) {
    //         record = existingRecord.get();
    //     } else {
    //         record = new HabitRecord();
    //         record.setUserId(user.getId());
    //         record.setDate(date);
    //     }
        
    //     record.setSleepMinutes(minutes);
    //     record.setOverallScore(null); // Clear score - will be recalculated by cron job
        
    //     return habitRecordRepository.save(record);
    // }

    // /**
    //  * Updates hydration milliliters
    //  * @param user The user
    //  * @param date The date
    //  * @param milliliters Milliliters consumed
    //  * @return The updated habit record
    //  */
    // public HabitRecord updateHydration(User user, LocalDate date, Integer milliliters) {
    //     Optional<HabitRecord> existingRecord = habitRecordRepository.findByUserIdAndDate(user.getId(), date);
        
    //     HabitRecord record;
    //     if (existingRecord.isPresent()) {
    //         record = existingRecord.get();
    //     } else {
    //         record = new HabitRecord();
    //         record.setUserId(user.getId());
    //         record.setDate(date);
    //     }
        
    //     record.setHydrationMl(milliliters);
    //     record.setOverallScore(null); // Clear score - will be recalculated by cron job
        
    //     return habitRecordRepository.save(record);
    // }



    // /**
    //  * Gets a habit record for a specific user and date
    //  * @param user The user
    //  * @param date The date
    //  * @return The habit record if found, empty if not
    //  */
    // public Optional<HabitRecord> getHabitRecord(User user, LocalDate date) {
    //     return habitRecordRepository.findByUserIdAndDate(user.getId(), date);
    // }

    // /**
    //  * Gets all habit records for a user, ordered by date (newest first)
    //  * @param user The user
    //  * @return List of habit records
    //  */
    // public List<HabitRecord> getUserHabitRecords(User user) {
    //     return habitRecordRepository.findByUserIdOrderByDateDesc(user.getId());
    // }

    // /**
    //  * Gets habit records for a user within a date range
    //  * @param user The user
    //  * @param startDate Start date (inclusive)
    //  * @param endDate End date (inclusive)
    //  * @return List of habit records in the date range
    //  */
    // public List<HabitRecord> getHabitRecordsByDateRange(User user, LocalDate startDate, LocalDate endDate) {
    //     return habitRecordRepository.findByUserIdAndDateBetween(user.getId(), startDate, endDate);
    // }

    // /**
    //  * Gets the user's average overall score
    //  * @param user The user
    //  * @return Average overall score, or 0.0 if no records
    //  */
    // public Double getAverageOverallScore(User user) {
    //     List<HabitRecord> records = habitRecordRepository.findOverallScoresByUserId(user.getId());
    //     if (records.isEmpty()) return 0.0;
        
    //     return records.stream()
    //             .filter(r -> r.getOverallScore() != null)
    //             .mapToDouble(r -> r.getOverallScore().doubleValue())
    //             .average()
    //             .orElse(0.0);
    // }

    // /**
    //  * Gets the user's average score for a specific area
    //  * @param user The user
    //  * @param area The habit area (nutrition, exercise, sleep, hydration)
    //  * @return Average score for the area, or 0.0 if no records
    //  */
    // public Double getAverageScoreForArea(User user, String area) {
    //     List<HabitRecord> records = getUserHabitRecords(user);
        
    //     if (records.isEmpty()) return 0.0;
        
    //     return records.stream()
    //             .mapToDouble(record -> {
    //                 switch (area.toLowerCase()) {
    //                     case "nutrition":
    //                         return habitScoreCalculator.calculateNutritionScore(record);
    //                     case "exercise":
    //                         return habitScoreCalculator.calculateExerciseScore(record);
    //                     case "sleep":
    //                         return habitScoreCalculator.calculateSleepScore(record);
    //                     case "hydration":
    //                         return habitScoreCalculator.calculateHydrationScore(record);
    //                     default:
    //                         return 0.0;
    //                 }
    //             })
    //             .average()
    //             .orElse(0.0);
    // }

    // /**
    //  * Deletes a habit record
    //  * @param recordId The record ID
    //  * @param user The user (for security check)
    //  * @return true if deleted, false if not found or not owned by user
    //  */
    // public boolean deleteHabitRecord(String recordId, User user) {
    //     Optional<HabitRecord> recordOpt = habitRecordRepository.findById(recordId);
        
    //     if (recordOpt.isPresent() && recordOpt.get().getUserId().equals(user.getId())) {
    //         habitRecordRepository.delete(recordOpt.get());
    //         return true;
    //     }
        
    //     return false;
    // }

    // /**
    //  * Gets habit statistics for a user
    //  * @param user The user
    //  * @return Map containing various statistics
    //  */
    // public java.util.Map<String, Object> getHabitStatistics(User user) {
    //     List<HabitRecord> records = getUserHabitRecords(user);
        
    //     if (records.isEmpty()) {
    //         return java.util.Map.of(
    //             "totalRecords", 0,
    //             "averageOverallScore", 0.0
    //         );
    //     }
        
    //     double averageOverallScore = getAverageOverallScore(user);
        
    //     return java.util.Map.of(
    //         "totalRecords", records.size(),
    //         "averageOverallScore", Math.round(averageOverallScore * 100.0) / 100.0,
    //         "averageNutrition", Math.round(getAverageScoreForArea(user, "nutrition") * 100.0) / 100.0,
    //         "averageExercise", Math.round(getAverageScoreForArea(user, "exercise") * 100.0) / 100.0,
    //         "averageSleep", Math.round(getAverageScoreForArea(user, "sleep") * 100.0) / 100.0,
    //         "averageHydration", Math.round(getAverageScoreForArea(user, "hydration") * 100.0) / 100.0
    //     );
    // }

    // // ========== METHODS FOR CONTROLLER ==========

    // /**
    //  * Get all habit records
    //  */
    // public List<HabitRecord> getAllHabitRecords() {
    //     return habitRecordRepository.findAll();
    // }

    // /**
    //  * Get habit record by ID
    //  */
    // public Optional<HabitRecord> getHabitRecordById(String id) {
    //     return habitRecordRepository.findById(id);
    // }

    // /**
    //  * Get habit records by user
    //  */
    // public List<HabitRecord> getHabitRecordsByUser(String userId) {
    //     return habitRecordRepository.findByUserIdOrderByDateDesc(userId);
    // }

    // /**
    //  * Get habit records by user and date range
    //  */
    // public List<HabitRecord> getHabitRecordsByUserAndDateRange(String userId, LocalDate startDate, LocalDate endDate) {
    //     return habitRecordRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
    // }
    
    // /**
    //  * Update individual nutrition meal for a user on a specific date
    //  * This preserves existing meal selections
    //  */
    // public HabitRecord updateIndividualMeal(String userId, LocalDate date, String mealType, boolean consumed) {
    //     Optional<HabitRecord> existingRecord = habitRecordRepository.findByUserIdAndDate(userId, date);
        
    //     HabitRecord record;
    //     if (existingRecord.isPresent()) {
    //         record = existingRecord.get();
    //     } else {
    //         record = new HabitRecord();
    //         record.setUserId(userId);
    //         record.setDate(date);
    //     }
        
    //     // Initialize nutrition meals if null
    //     if (record.getNutritionMeals() == null) {
    //         record.setNutritionMeals(new com.avenaio.technical_test.model.NutritionMeals());
    //     }
        
    //     // Update only the specific meal
    //     switch (mealType.toLowerCase()) {
    //         case "breakfast":
    //             record.getNutritionMeals().setBreakfast(consumed);
    //             break;
    //         case "snack_one":
    //             record.getNutritionMeals().setSnackOne(consumed);
    //             break;
    //         case "meal":
    //             record.getNutritionMeals().setMeal(consumed);
    //             break;
    //         case "snack_two":
    //             record.getNutritionMeals().setSnackTwo(consumed);
    //             break;
    //         case "dinner":
    //             record.getNutritionMeals().setDinner(consumed);
    //             break;
    //         default:
    //             throw new IllegalArgumentException("Invalid meal type: " + mealType);
    //     }
        
    //     // Clear score to trigger recalculation
    //     record.setOverallScore(null);
        
    //     return habitRecordRepository.save(record);
    // }

    // /**
    //  * Create or update a habit record (upsert operation)
    //  * This prevents duplicate records for the same user and date
    //  */
    // public HabitRecord createOrUpdateHabitRecord(HabitRecord habitRecord) {
    //     Optional<HabitRecord> existingRecord = habitRecordRepository.findByUserIdAndDate(
    //         habitRecord.getUserId(), habitRecord.getDate());
        
    //     if (existingRecord.isPresent()) {
    //         // Update existing record - preserve existing values if new ones are null
    //         HabitRecord existing = existingRecord.get();
            
    //         // Only update nutrition meals if provided and preserve existing selections
    //         if (habitRecord.getNutritionMeals() != null) {
    //             if (existing.getNutritionMeals() == null) {
    //                 existing.setNutritionMeals(habitRecord.getNutritionMeals());
    //             } else {
    //                 // Preserve existing meal selections, only update new ones
    //                 var existingMeals = existing.getNutritionMeals();
    //                 var newMeals = habitRecord.getNutritionMeals();
                    
    //                 // Only update if the new value is true (meal was just marked)
    //                 if (newMeals.isBreakfast()) existingMeals.setBreakfast(true);
    //                 if (newMeals.isSnackOne()) existingMeals.setSnackOne(true);
    //                 if (newMeals.isMeal()) existingMeals.setMeal(true);
    //                 if (newMeals.isSnackTwo()) existingMeals.setSnackTwo(true);
    //                 if (newMeals.isDinner()) existingMeals.setDinner(true);
    //             }
    //         }
            
    //         // Update other fields only if provided
    //         if (habitRecord.getExerciseMinutes() != null) {
    //             existing.setExerciseMinutes(habitRecord.getExerciseMinutes());
    //         }
    //         if (habitRecord.getSleepMinutes() != null) {
    //             existing.setSleepMinutes(habitRecord.getSleepMinutes());
    //         }
    //         if (habitRecord.getHydrationMl() != null) {
    //             existing.setHydrationMl(habitRecord.getHydrationMl());
    //         }
    //         if (habitRecord.getNotes() != null) {
    //             existing.setNotes(habitRecord.getNotes());
    //         }
            
    //         // Clear score to trigger recalculation
    //         existing.setOverallScore(null);
    //         return habitRecordRepository.save(existing);
    //     } else {
    //         // Create new record
    //         return habitRecordRepository.save(habitRecord);
    //     }
    // }
}
