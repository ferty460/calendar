package com.example.calendar.service;

import com.example.calendar.model.CustomOAuth2User;
import com.example.calendar.model.Role;
import com.example.calendar.model.User;
import com.example.calendar.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        return processOAuth2User(user);
    }

    private OAuth2User processOAuth2User(OAuth2User oauth2User) {
        CustomOAuth2User customOauth2User = new CustomOAuth2User(oauth2User);

        User user = userRepository.findByEmail(customOauth2User.getEmail());
        if (user == null) {
            user = new User();
            user.setEmail(customOauth2User.getEmail());
            user.setUsername(customOauth2User.getName());
            user.setActive(customOauth2User.isEnabled());
            user.getRoles().add(Role.ROLE_USER);

            // Generate a random password
            String password = UUID.randomUUID().toString();
            user.setPassword(password);

            userRepository.save(user);
        }

        return customOauth2User;
    }
}