package com.avenaio.technical_test.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.avenaio.technical_test.model.HabitDate;
import com.avenaio.technical_test.repository.HabitDateRepository;
import org.springframework.stereotype.Service;

@Service
public class HabitDateService {

    private final HabitDateRepository habitDateRepository;

    HabitDateService(HabitDateRepository habitDateRepository) {
        this.habitDateRepository = habitDateRepository;
    }
    // Get the list by date
    public List<HabitDate> getHabitDatesByDate(LocalDate registrationDate) {
        return habitDateRepository.findByRegistrationDate(registrationDate);
    }

    // Get by ID
    public HabitDate getHabitById(String id) {
        return habitDateRepository.findById(id).orElse(null);
    }

    // Update by habit date
    public HabitDate updateHabitDate(HabitDate habitDate) {
        return habitDateRepository.save(habitDate);
    }
}
