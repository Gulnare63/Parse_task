package com.example.parse_task;

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

    @Override
    public String toString() {
        return "From: " + from + "\n" +
               "Flight: " + flightNumber + "\n" +
               "Date: " + date + "\n" +
               "Time: " + time;
    }
}
