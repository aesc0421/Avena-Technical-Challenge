package com.avenaio.technical_test.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.avenaio.technical_test.model.HabitDate;

@Repository
public interface HabitDateRepository extends MongoRepository<HabitDate, String> {
    List<HabitDate> findByRegistrationDate(LocalDate registrationDate);
}
