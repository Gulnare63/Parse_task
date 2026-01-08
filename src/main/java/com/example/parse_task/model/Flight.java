package com.example.parse_task.model;

public class Flight {
    private String from;
    private String flightNumber;
    private String date;
    private String time;

    public Flight(String from, String flightNumber, String date, String time) {
        this.from = from;
        this.flightNumber = flightNumber;
        this.date = date;
        this.time = time;
    }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
