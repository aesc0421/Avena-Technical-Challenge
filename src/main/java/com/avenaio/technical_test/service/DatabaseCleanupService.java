package com.avenaio.technical_test.service;

import com.avenaio.technical_test.model.HabitRecord;
import com.avenaio.technical_test.repository.HabitRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service for cleaning up duplicate records and maintaining database integrity
 */
@Service
public class DatabaseCleanupService {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseCleanupService.class);

    @Autowired
    private HabitRecordRepository habitRecordRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Remove duplicate habit records for the same user and date
     * Keeps the most recent record (by createdAt timestamp)
     */
    public void removeDuplicateHabitRecords() {
        logger.info("Starting cleanup of duplicate habit records");

        try {
            // Find all habit records grouped by userId and date
            Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("userId", "date")
                    .count().as("count")
                    .first("userId").as("userId")
                    .first("date").as("date"),
                Aggregation.match(Criteria.where("count").gt(1))
            );

            AggregationResults<Map> results = mongoTemplate.aggregate(
                aggregation, "habit_records", Map.class);

            int duplicatesRemoved = 0;

            for (Map result : results) {
                String userId = (String) result.get("userId");
                LocalDate date = (LocalDate) result.get("date");
                int count = (Integer) result.get("count");

                logger.info("Found {} duplicate records for user {} on date {}", count, userId, date);

                // Get all records for this user and date, ordered by createdAt desc
                List<HabitRecord> duplicates = habitRecordRepository.findByUserIdAndDateOrderByCreatedAtDesc(userId, date);

                if (duplicates.size() > 1) {
                    // Keep the first one (most recent), delete the rest
                    for (int i = 1; i < duplicates.size(); i++) {
                        habitRecordRepository.delete(duplicates.get(i));
                        duplicatesRemoved++;
                        logger.info("Removed duplicate habit record: {} for user {} on date {}", 
                                   duplicates.get(i).getId(), userId, date);
                    }
                }
            }

            logger.info("Cleanup completed. Removed {} duplicate habit records", duplicatesRemoved);

        } catch (Exception e) {
            logger.error("Error during duplicate cleanup", e);
            throw new RuntimeException("Failed to cleanup duplicate records", e);
        }
    }

    /**
     * Clean up old habit records (older than specified days)
     */
    public void cleanupOldRecords(int daysToKeep) {
        LocalDate cutoffDate = LocalDate.now().minusDays(daysToKeep);
        
        logger.info("Cleaning up habit records older than {} (keeping {} days)", cutoffDate, daysToKeep);
        
        Query query = new Query(Criteria.where("date").lt(cutoffDate));
        List<HabitRecord> oldRecords = mongoTemplate.find(query, HabitRecord.class);
        
        if (!oldRecords.isEmpty()) {
            mongoTemplate.remove(query, HabitRecord.class);
            logger.info("Removed {} old habit records", oldRecords.size());
        } else {
            logger.info("No old records to clean up");
        }
    }

    /**
     * Get database statistics
     */
    public Map<String, Object> getDatabaseStats() {
        long totalRecords = habitRecordRepository.count();
        
        // Count records by date
        Aggregation dateAggregation = Aggregation.newAggregation(
            Aggregation.group("date").count().as("count"),
            Aggregation.sort(org.springframework.data.domain.Sort.Direction.DESC, "date")
        );
        
        AggregationResults<Map> dateResults = mongoTemplate.aggregate(
            dateAggregation, "habit_records", Map.class);

        return Map.of(
            "totalRecords", totalRecords,
            "recordsByDate", dateResults.getMappedResults()
        );
    }
}