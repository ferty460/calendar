package com.example.calendar.controller;

import com.example.calendar.model.Role;
import com.example.calendar.model.User;
import com.example.calendar.service.EventService;
import com.example.calendar.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private final UserService userService;
    private final EventService eventService;

    @GetMapping("/user/{user}")
    public String userInfo(@PathVariable("user") User user, Model model, Principal principal) {
        model.addAttribute("user", eventService.getUserByPrincipal(principal));
        model.addAttribute("username", user.getUsername());
        model.addAttribute("events", user.getEvents());
        if (user.getEvents().isEmpty()) model.addAttribute("error", "Событий нет");

        return "user-info";
    }

    @GetMapping("/admin")
    public String admin(Model model, Principal principal) {
        model.addAttribute("user", eventService.getUserByPrincipal(principal));
        model.addAttribute("users", userService.getAllUsers());

        return "admin";
    }

    @PostMapping("/admin/user/ban/{id}")
    public String userBan(@PathVariable("id") Long id) {
        userService.banUser(id);

        return "redirect:/admin";
    }
}
