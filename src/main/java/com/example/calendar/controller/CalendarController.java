package com.example.calendar.controller;

import com.example.calendar.model.date.Day;
import com.example.calendar.model.date.Month;
import com.example.calendar.model.date.Week;
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
