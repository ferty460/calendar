package com.example.calendar.model.date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Week {

    private List<Day> days;

    public Week() {
        this.days = new ArrayList<>(); // Инициализация списка в конструкторе
    }

}
