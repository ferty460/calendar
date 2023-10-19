package com.example.calendar.service;

import com.example.calendar.model.Event;
import com.example.calendar.model.Image;
import com.example.calendar.repo.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public void save(Event event, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        // todo: пользователь (см. buysell)
        addImage(file1, true, event);
        addImage(file2, false, event);
        addImage(file3, false, event);

        Event eventFromDb = eventRepository.save(event);
        eventFromDb.setPreviewImageId(eventFromDb.getImages().get(0).getId());
        eventRepository.save(event);
        log.info("Saving new Event: id {}, name {}, description {}, date {}", event.getId(), event.getName(), event.getDescription(), event.getDate());
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
        log.info("Deleting Event with id: {}", id);
    }

    public List<Event> getNearestEvents() {
        LocalDate localDate = LocalDate.now().minusDays(1);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return eventRepository.findAllByDateAfterOrderByDateAsc(date);
    }

    private void addImage(MultipartFile file, boolean preview, Event event) throws IOException {
        Image image;
        if (file.getSize() != 0) {
            image = toImageEntity(file);
            image.setPreviewImage(preview);
            event.addImageToProduct(image);
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
