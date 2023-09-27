package com.example.calendar.util;

import com.example.calendar.model.Event;
import com.example.calendar.model.date.Day;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CalendarUtil {

    public String checkEventsInDay(List<Event> events, Day day) {
        StringBuilder result = new StringBuilder();
        for (Event event : events) {
            if (event.getDate().toString().substring(8, 10).replaceAll("^0+","").equals("" + day.getDayOfMonth())) {
                result.append("\n");
                result.append(event.getName());
            }
        }

        return result.toString();
    }
}
