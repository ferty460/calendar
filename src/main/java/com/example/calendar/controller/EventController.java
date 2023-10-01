package com.example.calendar.controller;

import com.example.calendar.model.Event;
import com.example.calendar.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @GetMapping("/event")
    public String getAllEvents(Model model) {
        model.addAttribute("events", eventService.getAllEvents());
        model.addAttribute("event", new Event());

        return "event";
    }

    @GetMapping("/event/{id}")
    public String eventInfo(@PathVariable Long id, Model model) {
        model.addAttribute("event", eventService.getEventById(id));
        model.addAttribute("images", eventService.getEventById(id).getImages());

        return "event-info";
    }

    @PostMapping("/event/add")
    public String addEvent(
            @ModelAttribute("event") Event event,
            @RequestParam("dateString") String dateString,
            @RequestParam("file1") MultipartFile file1,
            @RequestParam("file2") MultipartFile file2,
            @RequestParam("file3") MultipartFile file3,
            Model model) throws IOException {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(dateString);
            event.setDate(date);
            eventService.save(event, file1, file2, file3);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        model.addAttribute("events", eventService.getAllEvents());

        return "redirect:/event";
    }

    @PostMapping("/event/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "redirect:/event";
    }
}
