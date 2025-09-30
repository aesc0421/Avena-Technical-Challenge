package com.avenaio.technical_test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents calculated scores for a specific time period
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScorePeriod {

    @Field("score_id")
    private String scoreId;

    @Field("period_id")
    private String periodId; // References the HabitPeriod this score is for

    @Field("calculation_time")
    private LocalDateTime calculationTime;

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
    @Field("overall_score")
    private BigDecimal overallScore;

    @Field("notes")
    private String notes;

    @Field("has_data")
    private boolean hasData = true; // Indicates if this score is based on actual habit data

    @Field("data_status")
    private String dataStatus = "COMPLETE"; // COMPLETE, NO_DATA, PARTIAL

    /**
     * Constructor for creating a new score period
     */
    public ScorePeriod(String scoreId, String periodId, Integer nutritionScore, 
                      Integer exerciseScore, Integer sleepScore, Integer hydrationScore, 
                      BigDecimal overallScore) {
        this.scoreId = scoreId;
        this.periodId = periodId;
        this.nutritionScore = nutritionScore;
        this.exerciseScore = exerciseScore;
        this.sleepScore = sleepScore;
        this.hydrationScore = hydrationScore;
        this.overallScore = overallScore;
        this.calculationTime = LocalDateTime.now();
        this.hasData = hasAnyHabitData();
        this.dataStatus = determineDataStatus();
        this.notes = generateNotes();
    }
    
    /**
     * Check if this score period has any actual habit data
     */
    private boolean hasAnyHabitData() {
        return nutritionScore > 0 || exerciseScore > 0 || sleepScore > 0 || hydrationScore > 0;
    }
    
    /**
     * Determine the data status based on scores
     */
    private String determineDataStatus() {
        if (!hasAnyHabitData()) {
            return "NO_DATA";
        }
        
        int nonZeroScores = 0;
        if (nutritionScore > 0) nonZeroScores++;
        if (exerciseScore > 0) nonZeroScores++;
        if (sleepScore > 0) nonZeroScores++;
        if (hydrationScore > 0) nonZeroScores++;
        
        return nonZeroScores == 4 ? "COMPLETE" : "PARTIAL";
    }
    
    /**
     * Generate appropriate notes based on data status
     */
    private String generateNotes() {
        String baseNote = "Score calculated at " + calculationTime;
        
        switch (dataStatus) {
            case "NO_DATA":
                return baseNote + " - No habit information available for this period";
            case "PARTIAL":
                return baseNote + " - Based on partial habit data";
            case "COMPLETE":
            default:
                return baseNote + " - Based on complete habit data";
        }
    }
}