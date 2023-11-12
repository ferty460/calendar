package com.example.calendar.controller;

import com.example.calendar.model.Event;
import com.example.calendar.model.User;
import com.example.calendar.service.EventService;
import com.example.calendar.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

@Controller
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final UserService userService;

    @GetMapping("/")
    public String main() {
        return "main";
    }

    @GetMapping("/event")
    public String getAllEvents(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.getUserByUsername(userDetails.getUsername());
        model.addAttribute("events", eventService.getAllByUser(user));
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
            @ModelAttribute("event") Event event, @RequestParam("dateString") String dateString,
            @RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
            @RequestParam("file3") MultipartFile file3, @AuthenticationPrincipal UserDetails userDetails, Model model
    ) throws IOException, ParseException {
        User user = userService.getUserByUsername(userDetails.getUsername());
        eventService.save(user, dateString, event, file1, file2, file3);

        model.addAttribute("events", eventService.getAllByUser(user));

        return "redirect:/event";
    }

    @PostMapping("/event/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "redirect:/event";
    }
}
