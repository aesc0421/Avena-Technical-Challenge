package com.avenaio.technical_test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avenaio.technical_test.model.HabitRecord;
import com.avenaio.technical_test.model.User;
import com.avenaio.technical_test.repository.UserRepository;
import com.avenaio.technical_test.service.HabitRecordService;
import com.avenaio.technical_test.service.HabitSessionService;
import com.avenaio.technical_test.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/habits")
public class HabitController {
    // @Autowired
    // private HabitRecordService habitRecordService;

    // @Autowired
    // private HabitSessionService habitSessionService;

    // @Autowired
    // private UserService userService;
    


    // @PostMapping("/update-exercise")
    // public ResponseEntity<?> updateExercise(@RequestBody Map<String, String> request) {
    //     try {
    //         String userId = request.get("userId");
    //         Integer minutes = Integer.parseInt(request.get("minutes"));

    //         LocalDate date = LocalDate.now();

    //         User user = userService.findById(userId).orElse(null);
    //         if (user == null) {
    //             return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
    //         }

    //         // Use session-based exercise update method
    //         var updatedSession = habitSessionService.updateExerciseInCurrentPeriod(userId, date, minutes);
    //         var currentPeriod = updatedSession.getCurrentHabitPeriod();
            
    //         return ResponseEntity.ok(Map.of(
    //             "message", "Exercise updated successfully in current period",
    //             "minutes", minutes,
    //             "sessionId", updatedSession.getId(),
    //             "periodId", currentPeriod.getPeriodId(),
    //             "currentPeriod", currentPeriod,
    //             "totalPeriods", updatedSession.getHabitPeriods().size()
    //         ));
    //     } catch (IllegalArgumentException e) {
    //         return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    //     }
    // }

    // @PostMapping("/update-sleep")
    // public ResponseEntity<?> updateSleep(@RequestBody Map<String, String> request) {
    //     try {
    //         String userId = request.get("userId");
    //         Integer minutes = Integer.parseInt(request.get("minutes"));

    //         LocalDate date = LocalDate.now();

    //         User user = userService.findById(userId).orElse(null);
    //         if (user == null) {
    //             return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
    //         }

    //         // Use session-based sleep update method
    //         var updatedSession = habitSessionService.updateSleepInCurrentPeriod(userId, date, minutes);
    //         var currentPeriod = updatedSession.getCurrentHabitPeriod();
            
    //         return ResponseEntity.ok(Map.of(
    //             "message", "Sleep updated successfully in current period",
    //             "minutes", minutes,
    //             "sessionId", updatedSession.getId(),
    //             "periodId", currentPeriod.getPeriodId(),
    //             "currentPeriod", currentPeriod,
    //             "totalPeriods", updatedSession.getHabitPeriods().size()
    //         ));
    //     } catch (IllegalArgumentException e) {
    //         return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    //     }
    // }

    // @PostMapping("/update-hydration")
    // public ResponseEntity<?> updateHydration(@RequestBody Map<String, String> request) {
    //     try {
    //         String userId = request.get("userId");
    //         Integer milliliters = Integer.parseInt(request.get("milliliters"));

    //         LocalDate date = LocalDate.now();

    //         User user = userService.findById(userId).orElse(null);
    //         if (user == null) {
    //             return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
    //         }

    //         // Use session-based hydration update method
    //         var updatedSession = habitSessionService.updateHydrationInCurrentPeriod(userId, date, milliliters);
    //         var currentPeriod = updatedSession.getCurrentHabitPeriod();
            
    //         return ResponseEntity.ok(Map.of(
    //             "message", "Hydration updated successfully in current period",
    //             "milliliters", milliliters,
    //             "sessionId", updatedSession.getId(),
    //             "periodId", currentPeriod.getPeriodId(),
    //             "currentPeriod", currentPeriod,
    //             "totalPeriods", updatedSession.getHabitPeriods().size()
    //         ));
    //     } catch (IllegalArgumentException e) {
    //         return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    //     }
    // }

    // @PostMapping("/create-daily-record")
    // public ResponseEntity<?> createDailyRecord(@RequestBody Map<String, Object> request) {
    //     try {
    //         String userId = (String) request.get("userId");
    //         LocalDate date = LocalDate.now(); // Always use current date

    //         User user = userService.findById(userId).orElse(null);
    //         if (user == null) {
    //             return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
    //         }

    //         // Check if record already exists for this date
    //         Optional<HabitRecord> existingRecord = habitRecordService.getHabitRecord(user, date);
    //         if (existingRecord.isPresent()) {
    //             return ResponseEntity.badRequest().body(Map.of("error", "Daily record already exists for this date"));
    //         }

    //         // Create new daily record
    //         HabitRecord dailyRecord = new HabitRecord();
    //         dailyRecord.setUserId(user.getId());
    //         dailyRecord.setDate(date);
    //         dailyRecord.setNotes((String) request.get("notes"));
            
    //         // Set nutrition meals from object
    //         if (request.get("meals") != null) {
    //             @SuppressWarnings("unchecked")
    //             Map<String, Object> mealsMap = (Map<String, Object>) request.get("meals");
                
    //             com.avenaio.technical_test.model.NutritionMeals nutritionMeals = new com.avenaio.technical_test.model.NutritionMeals();
    //             nutritionMeals.setBreakfast(Boolean.TRUE.equals(mealsMap.get("breakfast")));
    //             nutritionMeals.setSnackOne(Boolean.TRUE.equals(mealsMap.get("snackOne")));
    //             nutritionMeals.setMeal(Boolean.TRUE.equals(mealsMap.get("meal")));
    //             nutritionMeals.setSnackTwo(Boolean.TRUE.equals(mealsMap.get("snackTwo")));
    //             nutritionMeals.setDinner(Boolean.TRUE.equals(mealsMap.get("dinner")));
                
    //             dailyRecord.setNutritionMeals(nutritionMeals);
    //         }
            
    //         // Set exercise, sleep, and hydration (handles both String and Integer)
    //         if (request.get("exerciseMinutes") != null) {
    //             Object exerciseValue = request.get("exerciseMinutes");
    //             if (exerciseValue instanceof Integer) {
    //                 dailyRecord.setExerciseMinutes((Integer) exerciseValue);
    //             } else {
    //                 dailyRecord.setExerciseMinutes(Integer.parseInt(exerciseValue.toString()));
    //             }
    //         }
    //         if (request.get("sleepMinutes") != null) {
    //             Object sleepValue = request.get("sleepMinutes");
    //             if (sleepValue instanceof Integer) {
    //                 dailyRecord.setSleepMinutes((Integer) sleepValue);
    //             } else {
    //                 dailyRecord.setSleepMinutes(Integer.parseInt(sleepValue.toString()));
    //             }
    //         }
    //         if (request.get("hydrationMl") != null) {
    //             Object hydrationValue = request.get("hydrationMl");
    //             if (hydrationValue instanceof Integer) {
    //                 dailyRecord.setHydrationMl((Integer) hydrationValue);
    //             } else {
    //                 dailyRecord.setHydrationMl(Integer.parseInt(hydrationValue.toString()));
    //             }
    //         }
            
    //         // Save the record (score will be calculated by cron job)
    //         HabitRecord savedRecord = habitRecordService.saveHabitRecord(dailyRecord);
            
    //         return ResponseEntity.ok(Map.of(
    //             "message", "Daily record created successfully",
    //             "habitRecord", savedRecord
    //         ));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    //     }
    // }

    //     @PostMapping("/meals/register")
    //     public ResponseEntity<?> registerMeal(@RequestBody Map<String, String> request) {
    //         try {
    //             String userId = request.get("userId");
    //             String mealType = request.get("mealType");

    //             LocalDate date = LocalDate.now();

    //             User user = userService.findById(userId).orElse(null);
    //             if (user == null) {
    //                 return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
    //             }

    //             // Use the session-based meal update method (always updates current period)
    //             var updatedSession = habitSessionService.updateMealInCurrentPeriod(userId, date, mealType, true);
    //             var currentPeriod = updatedSession.getCurrentHabitPeriod();
                
    //             return ResponseEntity.ok(Map.of(
    //                 "message", "Meal registered successfully in current period",
    //                 "mealType", mealType,
    //                 "sessionId", updatedSession.getId(),
    //                 "periodId", currentPeriod.getPeriodId(),
    //                 "currentPeriod", currentPeriod,
    //                 "totalPeriods", updatedSession.getHabitPeriods().size()
    //             ));
    //         } catch (Exception e) {
    //             return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    //         }
    //     }

    // // ========== SESSION-BASED ENDPOINTS ==========

    // /**
    //  * Get habit session with all periods for a user on a specific date
    //  */
    // @GetMapping("/sessions/user/{userId}/date/{date}")
    // public ResponseEntity<?> getUserSessionByDate(@PathVariable String userId, @PathVariable String date) {
    //     try {
    //         LocalDate targetDate = LocalDate.parse(date);
    //         var session = habitSessionService.getSession(userId, targetDate);
            
    //         if (session.isPresent()) {
    //             return ResponseEntity.ok(Map.of(
    //                 "session", session.get(),
    //                 "totalPeriods", session.get().getHabitPeriods().size(),
    //                 "totalScores", session.get().getScorePeriods().size(),
    //                 "currentPeriodIndex", session.get().getCurrentPeriodIndex()
    //             ));
    //         } else {
    //             return ResponseEntity.ok(Map.of(
    //                 "message", "No session found for this date",
    //                 "userId", userId,
    //                 "date", date
    //             ));
    //         }
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    //     }
    // }

    // /**
    //  * Get all sessions for a user
    //  */
    // @GetMapping("/sessions/user/{userId}")
    // public ResponseEntity<?> getUserSessions(@PathVariable String userId) {
    //     try {
    //         var sessions = habitSessionService.getUserSessions(userId);
    //         return ResponseEntity.ok(Map.of(
    //             "sessions", sessions,
    //             "totalSessions", sessions.size()
    //         ));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    //     }
    // }

    // /**
    //  * Manually create a new period (for testing time lapse)
    //  */
    // @PostMapping("/sessions/new-period")
    // public ResponseEntity<?> createNewPeriod(@RequestBody Map<String, String> request) {
    //     try {
    //         String userId = request.get("userId");
    //         String reason = request.getOrDefault("reason", "Manual period creation");
    //         LocalDate date = LocalDate.now();
            
    //         var updatedSession = habitSessionService.createNewPeriod(userId, date, reason);
            
    //         return ResponseEntity.ok(Map.of(
    //             "message", "New period created successfully",
    //             "sessionId", updatedSession.getId(),
    //             "totalPeriods", updatedSession.getHabitPeriods().size(),
    //             "currentPeriodIndex", updatedSession.getCurrentPeriodIndex(),
    //             "reason", reason
    //         ));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    //     }
    // }

    // // ========== ENDPOINTS TO GET SCORES (SESSION-BASED) ==========

    // /**
    //  * Get latest scores for a user from current session
    //  */
    // @GetMapping("/scores/user/{userId}/latest")
    // public ResponseEntity<?> getLatestScoresByUser(@PathVariable String userId) {
    //     try {
    //         LocalDate today = LocalDate.now();
    //         var sessionOpt = habitSessionService.getSession(userId, today);
            
    //         if (sessionOpt.isEmpty()) {
    //             return ResponseEntity.ok(Map.of(
    //                 "message", "No session found for today",
    //                 "userId", userId,
    //                 "date", today
    //             ));
    //         }
            
    //         var session = sessionOpt.get();
    //         var latestScore = session.getLatestScorePeriod();
    //         var currentPeriod = session.getCurrentHabitPeriod();
            
    //         if (latestScore == null) {
    //             return ResponseEntity.ok(Map.of(
    //                 "message", "No scores calculated yet",
    //                 "userId", userId,
    //                 "sessionId", session.getId(),
    //                 "totalPeriods", session.getHabitPeriods().size()
    //             ));
    //         }
            
    //         Map<String, Object> response = buildScoreResponse(session, latestScore, currentPeriod);
    //         return ResponseEntity.ok(response);
            
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    //     }
    // }

    // /**
    //  * Get paginated scores for a user with period details
    //  * @param userId User ID
    //  * @param date Date (optional, defaults to today)
    //  * @param limit Maximum number of score periods to return (default: 10, max: 50)
    //  * @param offset Number of periods to skip (default: 0)
    //  */
    // @GetMapping("/scores/user/{userId}")
    // public ResponseEntity<?> getPaginatedScoresByUser(
    //         @PathVariable String userId,
    //         @RequestParam(required = false) String date,
    //         @RequestParam(defaultValue = "10") int limit,
    //         @RequestParam(defaultValue = "0") int offset) {
        
    //     try {
    //         // Validate and set limits
    //         if (limit > 50) limit = 50;
    //         if (limit < 1) limit = 1;
    //         if (offset < 0) offset = 0;
            
    //         LocalDate targetDate = date != null ? LocalDate.parse(date) : LocalDate.now();
    //         var sessionOpt = habitSessionService.getSession(userId, targetDate);
            
    //         if (sessionOpt.isEmpty()) {
    //             return ResponseEntity.ok(Map.of(
    //                 "message", "No session found",
    //                 "userId", userId,
    //                 "date", targetDate,
    //                 "scores", List.of(),
    //                 "pagination", Map.of(
    //                     "total", 0,
    //                     "limit", limit,
    //                     "offset", offset,
    //                     "hasMore", false
    //                 )
    //             ));
    //         }
            
    //         var session = sessionOpt.get();
    //         var allScores = session.getScorePeriods();
            
    //         // Apply pagination
    //         int total = allScores.size();
    //         int endIndex = Math.min(offset + limit, total);
    //         var paginatedScores = allScores.subList(offset, endIndex);
            
    //         // Build response with period details
    //         List<Map<String, Object>> scoreResponses = paginatedScores.stream()
    //             .map(scorePeriod -> {
    //                 // Find corresponding habit period
    //                 var habitPeriod = session.getHabitPeriods().stream()
    //                     .filter(hp -> hp.getPeriodId().equals(scorePeriod.getPeriodId()))
    //                     .findFirst().orElse(null);
                    
    //                 return buildScoreResponse(session, scorePeriod, habitPeriod);
    //             })
    //             .toList();
            
    //         return ResponseEntity.ok(Map.of(
    //             "userId", userId,
    //             "date", targetDate,
    //             "sessionId", session.getId(),
    //             "scores", scoreResponses,
    //             "pagination", Map.of(
    //                 "total", total,
    //                 "limit", limit,
    //                 "offset", offset,
    //                 "hasMore", endIndex < total,
    //                 "returned", paginatedScores.size()
    //             )
    //         ));
            
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    //     }
    // }

    // /**
    //  * Get scores across multiple dates with pagination
    //  */
    // @GetMapping("/scores/user/{userId}/history")
    // public ResponseEntity<?> getScoreHistory(
    //         @PathVariable String userId,
    //         @RequestParam(required = false) String startDate,
    //         @RequestParam(required = false) String endDate,
    //         @RequestParam(defaultValue = "5") int limit,
    //         @RequestParam(defaultValue = "0") int offset) {
        
    //     try {
    //         // Validate limits
    //         if (limit > 20) limit = 20;
    //         if (limit < 1) limit = 1;
    //         if (offset < 0) offset = 0;
            
    //         LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.now().minusDays(7);
    //         LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.now();
            
    //         var allSessions = habitSessionService.getUserSessions(userId);
            
    //         // Filter by date range and apply pagination
    //         var filteredSessions = allSessions.stream()
    //             .filter(session -> !session.getDate().isBefore(start) && !session.getDate().isAfter(end))
    //             .skip(offset)
    //             .limit(limit)
    //             .toList();
            
    //         List<Map<String, Object>> sessionSummaries = filteredSessions.stream()
    //             .map(session -> {
    //                 var latestScore = session.getLatestScorePeriod();
    //                 Map<String, Object> summary = new java.util.HashMap<>();
    //                 summary.put("date", session.getDate());
    //                 summary.put("sessionId", session.getId());
    //                 summary.put("totalPeriods", session.getHabitPeriods().size());
    //                 summary.put("totalScores", session.getScorePeriods().size());
    //                 summary.put("latestScore", latestScore != null ? latestScore.getOverallScore() : null);
    //                 summary.put("lastActivity", session.getLastActivityTime());
    //                 return summary;
    //             })
    //             .toList();
            
    //         return ResponseEntity.ok(Map.of(
    //             "userId", userId,
    //             "dateRange", Map.of("start", start, "end", end),
    //             "sessions", sessionSummaries,
    //             "pagination", Map.of(
    //                 "limit", limit,
    //                 "offset", offset,
    //                 "returned", filteredSessions.size(),
    //                 "hasMore", allSessions.size() > (offset + limit)
    //             )
    //         ));
            
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    //     }
    // }

    // /**
    //  * Helper method to build score response
    //  */
    // private Map<String, Object> buildScoreResponse(com.avenaio.technical_test.model.HabitSession session, 
    //                                               com.avenaio.technical_test.model.ScorePeriod scorePeriod, 
    //                                               com.avenaio.technical_test.model.HabitPeriod habitPeriod) {
    //     Map<String, Object> response = new java.util.HashMap<>();
        
    //     // Get user information
    //     Optional<User> userOpt = userRepository.findById(session.getUserId());
    //     User user = userOpt.orElse(null);
        
    //     // Challenge Backend format - dates in yyyyMMddTHHmmssZ format
    //     DateTimeFormatter challengeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
    //     String creationDate = scorePeriod.getCalculationTime() != null ? 
    //         scorePeriod.getCalculationTime().format(challengeFormatter) : 
    //         java.time.LocalDateTime.now().format(challengeFormatter);
        
    //     String registrationDate = (user != null && user.getCreatedAt() != null) ? 
    //         user.getCreatedAt().format(challengeFormatter) : creationDate;
        
    //     // Basic Challenge Backend fields
    //     response.put("creationDate", creationDate);
    //     response.put("registrationDate", registrationDate);
    //     response.put("patientId", user != null ? user.getId() : "");
    //     response.put("patientEmail", user != null ? user.getEmail() : "");
        
    //     // Day score (overall score from Challenge Backend specification)
    //     response.put("dayScore", scorePeriod.getOverallScore() != null ? 
    //         scorePeriod.getOverallScore().doubleValue() : 0.0);
        
    //     // Individual scores in Challenge Backend format
    //     Map<String, Integer> scores = Map.of(
    //         "nutrition", scorePeriod.getNutritionScore() != null ? scorePeriod.getNutritionScore() : 0,
    //         "exercise", scorePeriod.getExerciseScore() != null ? scorePeriod.getExerciseScore() : 0,
    //         "sleep", scorePeriod.getSleepScore() != null ? scorePeriod.getSleepScore() : 0,
    //         "hydration", scorePeriod.getHydrationScore() != null ? scorePeriod.getHydrationScore() : 0
    //     );
    //     response.put("score", scores);
        
    //     // Nutrition meals in Challenge Backend format (snake_case)
    //     Map<String, Boolean> nutrition;
    //     if (habitPeriod != null && habitPeriod.getNutritionMeals() != null) {
    //         nutrition = Map.of(
    //             "breakfast", habitPeriod.getNutritionMeals().isBreakfast(),
    //             "snack_one", habitPeriod.getNutritionMeals().isSnackOne(),
    //             "meal", habitPeriod.getNutritionMeals().isMeal(),
    //             "snack_two", habitPeriod.getNutritionMeals().isSnackTwo(),
    //             "dinner", habitPeriod.getNutritionMeals().isDinner()
    //         );
    //     } else {
    //         nutrition = Map.of(
    //             "breakfast", false,
    //             "snack_one", false,
    //             "meal", false,
    //             "snack_two", false,
    //             "dinner", false
    //         );
    //     }
    //     response.put("nutrition", nutrition);
        
    //     // Exercise, sleep, and hydration data in Challenge Backend format
    //     response.put("exerciseMinutes", habitPeriod != null && habitPeriod.getExerciseMinutes() != null ? 
    //         habitPeriod.getExerciseMinutes() : 0);
    //     response.put("sleepMinutes", habitPeriod != null && habitPeriod.getSleepMinutes() != null ? 
    //         habitPeriod.getSleepMinutes() : 0);
    //     response.put("hydrationML", habitPeriod != null && habitPeriod.getHydrationMl() != null ? 
    //         habitPeriod.getHydrationMl() : 0);
        
    //     return response;
    // }

}