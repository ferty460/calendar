package com.example.calendar.controller;

import com.example.calendar.model.User;
import com.example.calendar.model.date.Day;
import com.example.calendar.model.date.Month;
import com.example.calendar.model.date.Week;
import com.example.calendar.service.EventService;
import com.example.calendar.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CalendarController {

    private final EventService eventService;
    private final UserService userService;

    @GetMapping("/calendar")
    public String showCalendar(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        int year = LocalDate.now().getYear();
        List<Month> months = generateCalendarMonths(year);
        User user = userService.getUserByUsername(userDetails.getUsername());
        calendarDetails(user, months, model, year);

        return "calendar";
    }

    @GetMapping("/calendar/changeYear")
    public String changeCalendarYear(@RequestParam("year") Integer year, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (year == null) year = LocalDate.now().getYear();
        List<Month> months = generateCalendarMonths(year);
        User user = userService.getUserByUsername(userDetails.getUsername());
        calendarDetails(user, months, model, year);

        return "calendar";
    }

    private void calendarDetails(User user, List<Month> months, Model model, Integer year) {
        String[] monthNames = {
                "Январь", "Февраль",
                "Март", "Апрель", "Май",
                "Июнь", "Июль", "Август",
                "Сентябрь", "Октябрь", "Ноябрь",
                "Декабрь"
        };
        int[] years = {2023, 2024, 2025, 2026, 2027, 2028, 2029, 2030};

        model.addAttribute("monthNames", monthNames);
        model.addAttribute("months", months);
        model.addAttribute("events", eventService.getAllByUser(user));
        model.addAttribute("nearestEvents", eventService.getNearestEvents(user));
        model.addAttribute("years", years);
        model.addAttribute("selectedYear", year);
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
