package com.avenaio.technical_test.service;

import com.avenaio.technical_test.dto.DailyHabitProcessingMessage;
import com.avenaio.technical_test.dto.HabitScoreTaskMessage;
import com.avenaio.technical_test.model.HabitDate;
import com.avenaio.technical_test.model.HabitSession;
import com.avenaio.technical_test.repository.HabitDateRepository;
import com.avenaio.technical_test.repository.HabitSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service for processing daily habit records
 */
@Service
public class DailyHabitProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(DailyHabitProcessingService.class);
    private static final int MAX_RETRY_ATTEMPTS = 3;

    // @Autowired
    // private HabitSessionRepository habitSessionRepository;

    @Autowired
    private HabitDateService habitDateService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${habit.exchange}")
    private String exchangeName;

    /**
     * Process daily habit records task
     */
    @RabbitListener(queues = "${habit.queue.main}")
    public void processDailyHabitRecords(DailyHabitProcessingMessage message) {
        logger.info("Processing daily habit records for date: {} with taskId: {}", 
            message.getTargetDate(), message.getTaskId());
        
        try {
            // Get all habit sessions for the target date from all users
            List<HabitDate> habitDates = habitDateService.getHabitDatesByDate(message.getTargetDate());            // List<HabitSession> sessions = habitSessionRepository.findByDate(message.getTargetDate());
            logger.info("Found {} habit sessions for date: {}", habitDates.size(), message.getTargetDate());
            
            if (habitDates.isEmpty()) {
                logger.warn("No habit sessions found for date: {}. Second queue will not receive any messages.", message.getTargetDate());
                return;
            }
            
            int enqueuedTasks = 0;
            
            // Enqueue each habit session into the second queue (individual queue)
            for (HabitDate habitDate : habitDates) {
                // Only process sessions that don't have score periods yet
                if (habitDate.getScore() == null) {
                    String taskId = UUID.randomUUID().toString();
                    HabitScoreTaskMessage individualMessage = new HabitScoreTaskMessage(
                        habitDate.getId(),
                        habitDate.getPatientId(),
                        habitDate.getRegistrationDate(),
                        0,
                        taskId
                    );
                    
                    // Send to second queue (individual queue)
                    rabbitTemplate.convertAndSend(exchangeName, "habit.individual", individualMessage);
                    enqueuedTasks++;
                    
                    logger.info("✅ Enqueued to second queue - habit session: {} with taskId: {} for user: {}", 
                        habitDate.getId(), taskId, habitDate.getPatientId());
                } else {
                    logger.debug("Skipping habit session {} - scores already calculated", habitDate.getId());
                }
            }
            
            logger.info("Successfully processed daily habit records for date: {} with taskId: {}. Enqueued {} tasks to individual queue.", 
                message.getTargetDate(), message.getTaskId(), enqueuedTasks);
                
        } catch (Exception e) {
            logger.error("Error processing daily habit records for date: {} with taskId: {}", 
                message.getTargetDate(), message.getTaskId(), e);
            
            // Handle retry logic
            handleRetry(message, e);
        }
    }

    /**
     * Handle retry logic for failed tasks
     */
    private void handleRetry(DailyHabitProcessingMessage message, Exception error) {
        if (message.getRetryCount() < MAX_RETRY_ATTEMPTS) {
            message.setRetryCount(message.getRetryCount() + 1);
            logger.warn("Retrying daily habit processing for date: {} (attempt {}/{}) with taskId: {}", 
                message.getTargetDate(), message.getRetryCount(), MAX_RETRY_ATTEMPTS, message.getTaskId());
            
            try {
                logger.info("Would re-queue message for retry: {}", message);
            } catch (Exception retryError) {
                logger.error("Failed to re-queue message for retry: {}", message, retryError);
            }
        } else {
            logger.error("Max retry attempts reached for daily habit processing. Date: {} with taskId: {}", 
                message.getTargetDate(), message.getTaskId());
        }
    }
}