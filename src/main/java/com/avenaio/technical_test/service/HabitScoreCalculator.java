package com.avenaio.technical_test.service;

import com.avenaio.technical_test.model.HabitDate;
import com.avenaio.technical_test.model.HabitRecord;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Service for calculating habit scores - ONLY used by cron job
 */
@Service
public class HabitScoreCalculator {

    /**
     * Calculate nutrition score (0-100)
     * Each meal = 100 points, max 5 meals = 500 points, then average = 100
     */
    public int calculateNutritionScore(HabitDate habitDate) {
        if (habitDate.getNutrition() == null) return 0;
        
        int completedMeals = 0;
        if (habitDate.getNutrition().getBreakfast()) completedMeals++;
        if (habitDate.getNutrition().getSnackOne()) completedMeals++;
        if (habitDate.getNutrition().getMeal()) completedMeals++;
        if (habitDate.getNutrition().getSnackTwo()) completedMeals++;
        if (habitDate.getNutrition().getDinner()) completedMeals++;
        
        // Each meal = 100 points, average of 5 meals
        return (completedMeals * 100) / 5;
    }

    /**
     * Calculate exercise score (0-100)
     */
    public int calculateExerciseScore(HabitDate habitDate) {
        if (habitDate.getExerciseMinutes() == null) return 0;
        if (habitDate.getExerciseMinutes() >= 120) return 100; // 2 hours or more = 100 points
        return (habitDate.getExerciseMinutes() * 100) / 120; // Proportional calculation
    }

    /**
     * Calculate sleep score (0-100)
     */
    public int calculateSleepScore(HabitDate habitDate) {
        if (habitDate.getSleepMinutes() == null) return 0;
        if (habitDate.getSleepMinutes() >= 480) return 100; // 8 hours or more = 100 points
        return (habitDate.getSleepMinutes() * 100) / 480; // Proportional calculation
    }

    /**
     * Calculate hydration score (0-100)
     */
    public int calculateHydrationScore(HabitDate habitDate) {
        if (habitDate.getHydrationML() == null) return 0;
        if (habitDate.getHydrationML() >= 2000) return 100; // 2 liters or more = 100 points
        return (habitDate.getHydrationML() * 100) / 2000; // Proportional calculation
    }

    /**
     * Calculate overall score and set it on the record
     */
    public void calculateOverallScore(HabitDate habitDate) {
        int nutritionScore = calculateNutritionScore(habitDate);
        int exerciseScore = calculateExerciseScore(habitDate);
        int sleepScore = calculateSleepScore(habitDate);
        int hydrationScore = calculateHydrationScore(habitDate);

        int totalScore = nutritionScore + exerciseScore + sleepScore + hydrationScore;
        int count = 4; // Always 4 areas

        // habitDate.setDayScore(BigDecimal.valueOf(totalScore)
        //         .divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP));
        habitDate.setDayScore((double) totalScore / count);
    }
}