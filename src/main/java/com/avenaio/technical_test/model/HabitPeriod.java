package com.avenaio.technical_test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * Represents a time period within a habit session
 * Each period contains the habit data for that specific time frame
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitPeriod {

    @Field("period_id")
    private String periodId;

    @Field("start_time")
    private LocalDateTime startTime;

    @Field("end_time")
    private LocalDateTime endTime;

    @Field("nutrition_meals")
    private NutritionMeals nutritionMeals;

    @Field("exercise_minutes")
    private Integer exerciseMinutes = 0;

    @Field("sleep_minutes")
    private Integer sleepMinutes = 0;

    @Field("hydration_ml")
    private Integer hydrationMl = 0;

    @Field("is_active")
    private boolean isActive = true;

    @Field("notes")
    private String notes;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    /**
     * Constructor for creating a new period
     */
    public HabitPeriod(String periodId, LocalDateTime startTime) {
        this.periodId = periodId;
        this.startTime = startTime;
        this.nutritionMeals = new NutritionMeals();
        this.exerciseMinutes = 0;
        this.sleepMinutes = 0;
        this.hydrationMl = 0;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.notes = "Period created at " + startTime;
    }

    /**
     * Mark this period as ended
     */
    public void endPeriod() {
        this.endTime = LocalDateTime.now();
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Update the period's last modified time
     */
    public void touch() {
        this.updatedAt = LocalDateTime.now();
    }
}