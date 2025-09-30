package com.avenaio.technical_test.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Habit record entity that tracks all habit areas for a specific day
 */
@Document(collection = "habit_records")
@CompoundIndexes({
    @CompoundIndex(name = "user_date_idx", def = "{'userId': 1, 'date': 1}", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HabitRecord extends BaseEntity {

    @NotNull
    @Field("user_id")
    private String userId;

    @NotNull
    private LocalDate date;

    // Nutrition - individual meal tracking
    @Field("meals_count")
    private Integer mealsCount; // Number of meals consumed (0-5) - DEPRECATED, use individual meals
    
    @Field("nutrition_meals")
    private NutritionMeals nutritionMeals; // Individual meal tracking

    // Exercise - minutes of exercise
    @Field("exercise_minutes")
    private Integer exerciseMinutes;

    // Sleep - minutes of sleep
    @Field("sleep_minutes")
    private Integer sleepMinutes;

    // Hydration - milliliters consumed
    @Field("hydration_ml")
    private Integer hydrationMl;

    // Overall score calculated as average of all ratings
    @Field("overall_score")
    private BigDecimal overallScore;

    private String notes;
}