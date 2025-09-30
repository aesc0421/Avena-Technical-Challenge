package com.avenaio.technical_test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * HabitDate class representing daily habit data with scores
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "habit_dates")
public class HabitDate {
    @Id
    private String id;

    private LocalDate creationDate;
    private LocalDate registrationDate;
    private String patientId;
    private String patientEmail;
    private Double dayScore;
    private Score score;
    private Nutrition nutrition;
    private Integer exerciseMinutes;
    private Integer hydrationML;
    private Integer sleepMinutes;
    
    /**
     * Inner class for score breakdown
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Score {
        private Integer nutrition;
        private Integer exercise;
        private Integer sleep;
        private Integer hydration;
    }
    
    /**
     * Inner class for nutrition meals
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Nutrition {
        private Boolean breakfast;
        private Boolean snackOne;
        private Boolean meal;
        private Boolean snackTwo;
        private Boolean dinner;
    }
}
