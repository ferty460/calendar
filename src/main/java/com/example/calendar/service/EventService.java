package com.example.calendar.service;

import com.example.calendar.model.Event;
import com.example.calendar.repo.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public void save(Event event) {
        eventRepository.save(event);
        log.info("Saving new Event: {}", event);
    }
}
