package com.example.calendar.repo;

import com.example.calendar.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByDateAfterOrderByDateAsc(LocalDate currentDate);
}
