package com.example.calendar.controller;

import com.example.calendar.model.Event;
import com.example.calendar.model.User;
import com.example.calendar.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/")
    public String main(Principal principal, Model model) {
        model.addAttribute("user", eventService.getUserByPrincipal(principal));

        return "main";
    }

    @GetMapping("/event")
    public String getAllEvents(Principal principal, Model model) {
        User user = eventService.getUserByPrincipal(principal);
        model.addAttribute("events", eventService.getAllByUser(user));
        model.addAttribute("event", new Event());
        model.addAttribute("user", user);

        return "event";
    }

    @GetMapping("/event/{id}")
    public String eventInfo(@PathVariable Long id, Principal principal, Model model) {
        model.addAttribute("event", eventService.getEventById(id));
        model.addAttribute("images", eventService.getEventById(id).getImages());
        model.addAttribute("user", eventService.getUserByPrincipal(principal));

        return "event-info";
    }

    @PostMapping("/event/add")
    public String addEvent(
            @ModelAttribute("event") Event event, @RequestParam("dateString") String dateString,
            @RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
            @RequestParam("file3") MultipartFile file3, Principal principal, Model model
    ) throws IOException {
        if (file1.isEmpty() && file2.isEmpty() && file3.isEmpty()) {
            model.addAttribute("error", "Необходимо загрузить хотя бы один файл");
            return "redirect:/event";
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(dateString);
            event.setDate(date);
            eventService.save(principal, event, file1, file2, file3);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        model.addAttribute("events", eventService.getAllByUser(eventService.getUserByPrincipal(principal)));

        return "redirect:/event";
    }

    @PostMapping("/event/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "redirect:/event";
    }
}
