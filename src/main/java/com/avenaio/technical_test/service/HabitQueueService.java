package com.avenaio.technical_test.service;

import com.avenaio.technical_test.dto.DailyHabitProcessingMessage;
import com.avenaio.technical_test.dto.HabitScoreTaskMessage;
import com.avenaio.technical_test.model.HabitRecord;
import com.avenaio.technical_test.repository.HabitRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Service for managing habit score calculation queues
 */
@Service
public class HabitQueueService {

    private static final Logger logger = LoggerFactory.getLogger(HabitQueueService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private HabitRecordRepository habitRecordRepository;

    @Value("${habit.exchange}")
    private String exchangeName;

    @Value("${habit.queue.main}")
    private String mainQueueName;

    @Value("${habit.queue.individual}")
    private String individualQueueName;

    /**
     * Enqueue a daily habit processing task
     */
    public void enqueueDailyHabitProcessing(LocalDate targetDate) {
        String taskId = UUID.randomUUID().toString();
        DailyHabitProcessingMessage message = new DailyHabitProcessingMessage(targetDate, taskId, 0);
        
        rabbitTemplate.convertAndSend(exchangeName, "habit.main", message);
        // Task enqueued silently
    }

    /**
     * Process daily habit records and enqueue individual tasks
     */
    public void processDailyHabitRecords(LocalDate targetDate) {
        // Processing records silently
        
        try {
            // Get all habit records for the target date
            List<HabitRecord> records = habitRecordRepository.findByDate(targetDate);
            // Found records - processing silently
            
            int enqueuedTasks = 0;
            
            for (HabitRecord record : records) {
                // Only process records that don't have a calculated score yet
                if (record.getOverallScore() == null) {
                    String taskId = UUID.randomUUID().toString();
                    HabitScoreTaskMessage message = new HabitScoreTaskMessage(
                        record.getId(),
                        record.getUserId(),
                        record.getDate(),
                        0,
                        taskId
                    );
                    
                    rabbitTemplate.convertAndSend(exchangeName, "habit.individual", message);
                    enqueuedTasks++;
                    
                    logger.debug("Enqueued individual task for habit record: {} with taskId: {}", 
                        record.getId(), taskId);
                } else {
                    logger.debug("Skipping habit record {} - score already calculated: {}", 
                        record.getId(), record.getOverallScore());
                }
            }
            
            // Tasks enqueued silently
                
        } catch (Exception e) {
            logger.error("Error processing daily habit records for date: {}", targetDate, e);
            throw e;
        }
    }

    /**
     * Enqueue an individual habit score calculation task
     */
    public void enqueueIndividualHabitScoreCalculation(String habitRecordId, String userId, LocalDate date) {
        String taskId = UUID.randomUUID().toString();
        HabitScoreTaskMessage message = new HabitScoreTaskMessage(habitRecordId, userId, date, 0, taskId);
        
        rabbitTemplate.convertAndSend(exchangeName, "habit.individual", message);
        logger.info("Enqueued individual habit score calculation task for record: {} with taskId: {}", 
            habitRecordId, taskId);
    }
}