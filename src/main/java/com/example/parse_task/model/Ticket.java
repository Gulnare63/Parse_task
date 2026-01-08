package com.example.parse_task.model;

import java.util.List;

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

    public Ticket() {}

    // Getters və Setters
    // passenger, supplier, ticketNumber, flights və s.
    public String getPassenger() { return passenger; }
    public void setPassenger(String passenger) { this.passenger = passenger; }

    public String getSupplier() { return supplier; }
    public void setSupplier(String supplier) { this.supplier = supplier; }

    public String getTicketNumber() { return ticketNumber; }
    public void setTicketNumber(String ticketNumber) { this.ticketNumber = ticketNumber; }

    public String getBookingRef() { return bookingRef; }
    public void setBookingRef(String bookingRef) { this.bookingRef = bookingRef; }

    public String getIssuanceDate() { return issuanceDate; }
    public void setIssuanceDate(String issuanceDate) { this.issuanceDate = issuanceDate; }

    public String getPayment() { return payment; }
    public void setPayment(String payment) { this.payment = payment; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getBaseFare() { return baseFare; }
    public void setBaseFare(String baseFare) { this.baseFare = baseFare; }

    public String getTotalAmount() { return totalAmount; }
    public void setTotalAmount(String totalAmount) { this.totalAmount = totalAmount; }

    public String getJourneyType() { return journeyType; }
    public void setJourneyType(String journeyType) { this.journeyType = journeyType; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public List<Flight> getFlights() { return flights; }
    public void setFlights(List<Flight> flights) { this.flights = flights; }
}
