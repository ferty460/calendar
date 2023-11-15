package com.example.calendar.controller;

import com.example.calendar.model.User;
import com.example.calendar.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/registration")
    public String registerForm() {
        return "registration";
    }

    @PostMapping("/registration")
    public String processRegistration(User user, Model model) {
        if (!userService.save(user)) {
            model.addAttribute("error", "Ник " + user.getUsername() + " или email " + user.getEmail() + " уже занят!");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.getUserByUsername(userDetails.getUsername());

        model.addAttribute("user", user);
        model.addAttribute("events", user.getEvents());
        if (user.getEvents().isEmpty()) model.addAttribute("error", "Событий нет");

        return "profile";
    }

}
