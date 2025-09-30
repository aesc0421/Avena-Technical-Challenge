package com.avenaio.technical_test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Message for individual habit score calculation tasks
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitScoreTaskMessage {
    private String habitRecordId;
    private String userId;
    private LocalDate date;
    private int retryCount = 0;
    private String taskId;
}