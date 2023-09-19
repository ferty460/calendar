package com.example.calendar.controller;

import com.example.calendar.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        return "event";
    }

    @PostMapping("/event/add")
    public String addEvent(
            @RequestParam String name, @RequestParam String description, @RequestParam String date,
            Model model
    ) {
        eventService.save(name, description, date);
        model.addAttribute("events", eventService.getAllEvents());

        return "redirect:/event/all";
    }
}
