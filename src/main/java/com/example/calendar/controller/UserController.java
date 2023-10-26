package com.example.calendar.controller;

import com.example.calendar.model.User;
import com.example.calendar.service.EventService;
import com.example.calendar.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EventService eventService;

    @GetMapping("/login")
    public String loginForm(Principal principal, Model model) {
        model.addAttribute("user", eventService.getUserByPrincipal(principal));

        return "login";
    }

    @GetMapping("/registration")
    public String registerForm(Principal principal, Model model) {
        model.addAttribute("user", eventService.getUserByPrincipal(principal));

        return "registration";
    }

    @PostMapping("/registration")
    public String processRegistration(User user, Model model) {
        if (!userService.save(user)) {
            model.addAttribute("error", "Пользователь с ником " + user.getUsername() + " уже существует!");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        User user = eventService.getUserByPrincipal(principal);

        model.addAttribute("user", user);
        model.addAttribute("events", user.getEvents());
        if (user.getEvents().isEmpty()) model.addAttribute("error", "Событий нет");

        return "profile";
    }

}
