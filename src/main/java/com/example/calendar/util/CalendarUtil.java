package com.example.calendar.util;

import com.example.calendar.model.Event;
import com.example.calendar.model.date.Day;
import com.example.calendar.model.date.Month;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class CalendarUtil {

    public String checkEventsInDay(List<Event> events, Day day, Month month) {
        StringBuilder result = new StringBuilder();
        for (Event event : events) {
            if (getDay(event).equals(String.valueOf(day.getDayOfMonth()))
                    && getMonth(event).equals(String.valueOf(month.getMonth()))) {
                result.append("\n");
                result.append(event.getName());
            }
        }

        return result.toString();
    }

    public String checkEventsInDay(List<Event> events, String day, String month) {
        StringBuilder result = new StringBuilder();
        for (Event event : events) {
            if (getDay(event).equals(day)
                    && getMonth(event).equals(month)) {
                result.append("\n");
                result.append(event.getName());
            }
        }

        return result.toString();
    }

    public String getDayOfMonth() {
        return String.valueOf(LocalDate.now().getDayOfMonth());
    }

    public String getDayOfWeek() {
        String today = String.valueOf(LocalDate.now().getDayOfWeek());
        return switch (today) {
            case "MONDAY" -> "Понедельник";
            case "TUESDAY" -> "Вторник";
            case "WEDNESDAY" -> "Среда";
            case "THURSDAY" -> "Четверг";
            case "FRIDAY" -> "Пятница";
            case "SATURDAY" -> "Суббота";
            case "SUNDAY" -> "Воскресенье";
            default -> "a";
        };
    }

    public String getMonth() {
        String today = LocalDate.now().getMonth().toString();
        return switch (today) {
            case "JANUARY" -> "января";
            case "FEBRUARY" -> "февраля";
            case "MARCH" -> "марта";
            case "APRIL" -> "апреля";
            case "MAY" -> "мая";
            case "JUNE" -> "июня";
            case "JULY" -> "июля";
            case "AUGUST" -> "августа";
            case "SEPTEMBER" -> "сентября";
            case "OCTOBER" -> "октября";
            case "NOVEMBER" -> "ноября";
            case "DECEMBER" -> "декабря";
            default -> "a";
        };
    }

    public int getMonthValue() {
        return LocalDate.now().getMonth().getValue();
    }

    public String getDay(Event event) {
        return event.getDate().toString().substring(8,10).replaceAll("^0+","");
    }

    public String getMonth(Event event) {
        return event.getDate().toString().substring(5,7).replaceAll("^0+","");
    }
}
