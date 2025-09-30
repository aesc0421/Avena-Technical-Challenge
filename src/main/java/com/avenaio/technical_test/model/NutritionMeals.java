package com.avenaio.technical_test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Individual meal tracking for nutrition scoring
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NutritionMeals {
    
    @Field("breakfast")
    private boolean breakfast = false;
    
    @Field("snack_one")
    private boolean snackOne = false;
    
    @Field("meal")
    private boolean meal = false;
    
    @Field("snack_two")
    private boolean snackTwo = false;
    
    @Field("dinner")
    private boolean dinner = false;
    
    /**
     * Calculate nutrition score based on completed meals
     * Each meal = 20 points, max 5 meals = 100 points
     */
    public int calculateScore() {
        int completedMeals = 0;
        if (breakfast) completedMeals++;
        if (snackOne) completedMeals++;
        if (meal) completedMeals++;
        if (snackTwo) completedMeals++;
        if (dinner) completedMeals++;
        
        return (completedMeals * 100) / 5; // Each meal = 20 points
    }
    
    /**
     * Get total number of completed meals
     */
    public int getCompletedMealsCount() {
        int count = 0;
        if (breakfast) count++;
        if (snackOne) count++;
        if (meal) count++;
        if (snackTwo) count++;
        if (dinner) count++;
        return count;
    }
    
    /**
     * Check if any meal is completed
     */
    public boolean hasAnyMeal() {
        return breakfast || snackOne || meal || snackTwo || dinner;
    }
    
    /**
     * Check if all meals are completed
     */
    public boolean hasAllMeals() {
        return breakfast && snackOne && meal && snackTwo && dinner;
    }
}