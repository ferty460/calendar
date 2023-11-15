package com.example.calendar.service;

import com.example.calendar.model.Role;
import com.example.calendar.model.User;
import com.example.calendar.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean save(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null
                || userRepository.findByEmail(user.getEmail()) != null) return false;
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_ADMIN);
        userRepository.save(user);

        log.info("Saving new User with username: {}", user.getUsername());
        return true;
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
        log.info("Deleting user with id: {}", id);
    }

    public void banUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            if (user.isActive()) {
                user.setActive(false);
                log.info("Ban user with id = {}", user.getId());
            } else {
                user.setActive(true);
                log.info("Unban user with id = {}", user.getId());
            }
        }
        userRepository.save(user);
    }
}
