package com.example.parse_task.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {
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
    private String taxesFeesTotal;

    private List<FlightDTO> flights;

}
