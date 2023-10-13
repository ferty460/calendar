package com.example.calendar.controller;

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
        List<Month> months = generateCalendarMonths(LocalDate.now().getYear());
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
        model.addAttribute("nearestEvents", eventService.getNearestEvents());

        return "calendar";
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
        int startDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue();

        List<Day> days = new ArrayList<>();
        addEmptyDaysIfNecessary(startDayOfWeek, days);

        int currentDay = 1;
        int daysInMonth = firstDayOfMonth.lengthOfMonth();

        while (currentDay <= daysInMonth) {
            Day day = createDay(firstDayOfMonth, currentDay);
            days.add(day);
            currentDay++;

            if (days.size() == 7) {
                weeks.add(new Week(new ArrayList<>(days)));
                days.clear();
            }
        }

        addEmptyDaysAtMonthEnd(currentDay, days, daysInMonth, weeks);

        return weeks;
    }

    private void addEmptyDaysIfNecessary(int startDayOfWeek, List<Day> days) {
        if (startDayOfWeek != 1) {
            for (int i = 1; i < startDayOfWeek; i++) {
                Day emptyDay = new Day();
                emptyDay.setEmpty(true);
                days.add(emptyDay);
            }
        }
    }

    private Day createDay(LocalDate firstDayOfMonth, int currentDay) {
        Day day = new Day();
        day.setDayOfMonth(currentDay);
        day.setDayOfWeek(firstDayOfMonth.plusDays(currentDay - 1).getDayOfWeek().toString());
        return day;
    }

    private void addEmptyDaysAtMonthEnd(int currentDay, List<Day> days, int daysInMonth, List<Week> weeks) {
        if (!days.isEmpty() && currentDay > daysInMonth) {
            while (days.size() < 7) {
                Day emptyDay = new Day();
                emptyDay.setEmpty(true);
                days.add(emptyDay);
            }
            weeks.add(new Week(new ArrayList<>(days)));
        }
    }
}
