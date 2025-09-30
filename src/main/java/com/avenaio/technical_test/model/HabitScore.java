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
import java.time.LocalDate;

/**
 * Habit score entity that stores calculated scores for habit records
 * This is a separate collection to maintain historical score data
 */
@Document(collection = "habit_scores")
@CompoundIndexes({
    @CompoundIndex(name = "user_date_score_idx", def = "{'userId': 1, 'date': 1}", unique = true),
    @CompoundIndex(name = "date_idx", def = "{'date': 1}"),
    @CompoundIndex(name = "user_idx", def = "{'userId': 1}")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HabitScore extends BaseEntity {

    @NotNull
    @Field("user_id")
    private String userId;

    @NotNull
    private LocalDate date;

    @NotNull
    @Field("habit_record_id")
    private String habitRecordId;

    // Individual scores for each habit area (0-100)
    @Field("nutrition_score")
    private Integer nutritionScore;

    @Field("exercise_score")
    private Integer exerciseScore;

    @Field("sleep_score")
    private Integer sleepScore;

    @Field("hydration_score")
    private Integer hydrationScore;

    // Overall calculated score
    @NotNull
    @Field("overall_score")
    private BigDecimal overallScore;

    // Additional metadata
    @Field("calculation_timestamp")
    private java.time.LocalDateTime calculationTimestamp;

    private String notes;

    /**
     * Constructor for creating a new habit score from a habit record
     */
    public HabitScore(String userId, LocalDate date, String habitRecordId, 
                     Integer nutritionScore, Integer exerciseScore, 
                     Integer sleepScore, Integer hydrationScore, 
                     BigDecimal overallScore) {
        this.userId = userId;
        this.date = date;
        this.habitRecordId = habitRecordId;
        this.nutritionScore = nutritionScore;
        this.exerciseScore = exerciseScore;
        this.sleepScore = sleepScore;
        this.hydrationScore = hydrationScore;
        this.overallScore = overallScore;
        this.calculationTimestamp = java.time.LocalDateTime.now();
    }
}