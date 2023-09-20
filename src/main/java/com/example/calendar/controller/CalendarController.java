package com.example.calendar.controller;

import com.example.calendar.model.Event;
import com.example.calendar.model.date.Day;
import com.example.calendar.model.date.Month;
import com.example.calendar.model.date.Week;
import com.example.calendar.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CalendarController {

    private final EventService eventService;

    @GetMapping("/calendar")
    public String showCalendar(Model model) {
        List<Month> months = generateCalendarMonths(2023);
        String[] monthNames = {
                "Январь", "Февраль",
                "Март", "Апрель", "Май",
                "Июнь", "Июль", "Август",
                "Сентябрь", "Октябрь", "Ноябрь",
                "Декабрь"
        };

        model.addAttribute("monthNames", monthNames);
        model.addAttribute("months", months);
        model.addAttribute("events", eventService.getAllEvents());

        return "calendar";
    }

    public String getEventNames(List<Event> events) {
        StringBuilder sb = new StringBuilder();
        for (Event event : events) {
            sb.append(event.getName()).append(", ");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2); // Удаляем последнюю запятую и пробел
        }
        return sb.toString();
    }

    private List<Month> generateCalendarMonths(int year) {
        List<Month> months = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            Month currentMonth = new Month();
            currentMonth.setMonth(month);
            currentMonth.setYear(year);
            currentMonth.setWeeks(generateCalendarWeeks(month, year));
            months.add(currentMonth);
        }

        return months;
    }

    private List<Week> generateCalendarWeeks(int month, int year) {
        List<Week> weeks = new ArrayList<>();
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        int daysInMonth = firstDayOfMonth.lengthOfMonth();
        int currentDay = 1;

        while (currentDay <= daysInMonth) {
            Week week = new Week();
            List<Day> days = new ArrayList<>();

            for (int i = 0; i < 7; i++) {
                if (currentDay <= daysInMonth) {
                    Day day = new Day();
                    day.setDayOfMonth(currentDay);
                    day.setDayOfWeek(firstDayOfMonth.plusDays(currentDay - 1).getDayOfWeek().toString());
                    days.add(day);
                    currentDay++;
                }
            }
            week.setDays(days);
            weeks.add(week);
        }

        return weeks;
    }
}
