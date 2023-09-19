package com.example.calendar.model.date;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Month {

    private int month;
    private int year;
    private List<Week> weeks;

}
