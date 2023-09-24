package com.example.calendar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Date date;

    public String getFormattedDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return simpleDateFormat.format(date);
    }

    public Event(String name, String description, Date date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }
}
