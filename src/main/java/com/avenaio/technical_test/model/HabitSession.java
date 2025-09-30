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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Habit session entity that contains multiple time-based habit records
 * Each session represents a day, with multiple time periods within that day
 */
@Document(collection = "habit_sessions")
@CompoundIndexes({
    @CompoundIndex(name = "user_date_session_idx", def = "{'userId': 1, 'date': 1}", unique = true),
    @CompoundIndex(name = "user_idx", def = "{'userId': 1}"),
    @CompoundIndex(name = "date_idx", def = "{'date': 1}")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HabitSession extends BaseEntity {

    @NotNull
    @Field("user_id")
    private String userId;

    @NotNull
    private LocalDate date;

    @Field("habit_periods")
    private List<HabitPeriod> habitPeriods = new ArrayList<>();

    @Field("score_periods")
    private List<ScorePeriod> scorePeriods = new ArrayList<>();

    @Field("current_period_index")
    private Integer currentPeriodIndex = 0;

    @Field("session_start_time")
    private LocalDateTime sessionStartTime;

    @Field("last_activity_time")
    private LocalDateTime lastActivityTime;

    /**
     * Get the current active habit period
     */
    public HabitPeriod getCurrentHabitPeriod() {
        if (habitPeriods.isEmpty()) {
            return null;
        }
        if (currentPeriodIndex >= habitPeriods.size()) {
            return habitPeriods.get(habitPeriods.size() - 1);
        }
        return habitPeriods.get(currentPeriodIndex);
    }

    /**
     * Add a new habit period
     */
    public void addNewHabitPeriod(HabitPeriod period) {
        habitPeriods.add(period);
        currentPeriodIndex = habitPeriods.size() - 1;
        lastActivityTime = LocalDateTime.now();
    }

    /**
     * Add a new score period
     */
    public void addScorePeriod(ScorePeriod scorePeriod) {
        scorePeriods.add(scorePeriod);
    }

    /**
     * Get the latest score period
     */
    public ScorePeriod getLatestScorePeriod() {
        if (scorePeriods.isEmpty()) {
            return null;
        }
        return scorePeriods.get(scorePeriods.size() - 1);
    }
}