package com.example.parse_task.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Ticket {
    private String passenger;
    private String supplier;
    private String ticketNumber;
    private String bookingRef;
    private String issuanceDate;
    private String payment;
    private String currency;
    private String baseFare;
    private String totalAmount;
    private String journeyType;
    private String region;
    private List<Flight> flights;
    private String taxesFeesTotal;

    public Ticket() {
    }

}
