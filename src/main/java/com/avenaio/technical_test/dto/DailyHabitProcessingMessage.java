package com.avenaio.technical_test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Message for daily habit processing tasks
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyHabitProcessingMessage {
    private LocalDate targetDate;
    private String taskId;
    private int retryCount = 0;
}