package com.example.parse_task.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightDTO {
    private String from;
    private String to; // əlavə
    private String flightNumber;
    private String date;
    private String time;

    public FlightDTO() {
    }

    public FlightDTO(String from, String to, String flightNumber, String date, String time) {
        this.from = from;
        this.to = to;
        this.flightNumber = flightNumber;
        this.date = date;
        this.time = time;

    }
}
