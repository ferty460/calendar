package com.example.calendar.repo;

import com.example.calendar.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByDateAfterOrderByDateAsc(Date currentDate);
}
