package com.example.calendar.service;

import com.example.calendar.model.Event;
import com.example.calendar.model.Image;
import com.example.calendar.model.User;
import com.example.calendar.repo.EventRepository;
import com.example.calendar.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> getAllByUser(User user) {
        return eventRepository.findAllByUser(user);
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public void save(Principal principal, Event event, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        event.setUser(getUserByPrincipal(principal));
        addImage(file1, true, event);
        addImage(file2, false, event);
        addImage(file3, false, event);

        Event eventFromDb = eventRepository.save(event);
        eventFromDb.setPreviewImageId(eventFromDb.getImages().get(0).getId());
        eventRepository.save(event);
        log.info("Saving new Event with id: {}, name: {}, description: {}, date: {}, user: {}", event.getId(), event.getName(), event.getDescription(), event.getDate(), event.getUser().getUsername());
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByUsername(principal.getName());
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
        log.info("Deleting Event with id: {}", id);
    }

    public List<Event> getNearestEvents(User user) {
        LocalDate localDate = LocalDate.now().minusDays(1);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return eventRepository.findAllByUserAndDateAfterOrderByDateAsc(user, date);
    }

    private void addImage(MultipartFile file, boolean preview, Event event) throws IOException {
        Image image;
        if (file.getSize() != 0) {
            image = toImageEntity(file);
            image.setPreviewImage(preview);
            event.addImageToEvent(image);
        }
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());

        return image;
    }
}
