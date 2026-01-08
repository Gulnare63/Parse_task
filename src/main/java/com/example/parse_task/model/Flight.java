package com.example.parse_task.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Flight {
    private String from;
    private String to; // yeni əlavə
    private String flightNumber;
    private String date;
    private String time;

    public Flight(String from, String to ,String flightNumber, String date, String time) {
        this.from = from;
        this.to = to;
        this.flightNumber = flightNumber;
        this.date = date;
        this.time = time;

    }

}
