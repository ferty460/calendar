package com.example.calendar.controller;

import com.example.calendar.model.User;
import com.example.calendar.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
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
    public String processRegistration(User user) {
        userService.save(user);

        return "redirect:/login";
    }
}
