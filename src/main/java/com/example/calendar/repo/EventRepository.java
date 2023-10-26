package com.example.calendar.repo;

import com.example.calendar.model.Event;
import com.example.calendar.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByUserAndDateAfterOrderByDateAsc(User user, Date currentDate);


    List<Event> findAllByUser(User user);
}
