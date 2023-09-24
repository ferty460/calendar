package com.example.calendar.controller;

import com.example.calendar.model.Event;
import com.example.calendar.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/")
    public String main() {
        return "main";
    }

    @GetMapping("/event/all")
    public String getAllEvents(Model model) {
        model.addAttribute("events", eventService.getAllEvents());
        model.addAttribute("event", new Event());

        return "event";
    }

    @PostMapping("/event/add")
    public String addEvent(@ModelAttribute("event") Event event, @RequestParam("dateString") String dateString, Model model) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(dateString);
            event.setDate(date);
            eventService.save(event);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        model.addAttribute("events", eventService.getAllEvents());

        return "redirect:/event/all";
    }
}
