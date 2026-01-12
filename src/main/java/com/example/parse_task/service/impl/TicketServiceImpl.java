package com.example.parse_task.service.impl;

import com.example.parse_task.model.Ticket;
import com.example.parse_task.model.dto.FlightDTO;
import com.example.parse_task.model.dto.TicketDTO;
import com.example.parse_task.service.TicketService;
import com.example.parse_task.util.TicketParser;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Properties;

@Service
public class TicketServiceImpl implements TicketService {

    @Override
    public TicketDTO getTicketFromFile(MultipartFile file) {
        try {
            MimeMessage message = createMessage(file);
            String body = extractBody(message);
            Ticket ticket = TicketParser.parseTicket(body);

            return mapToDTO(ticket);

        } catch (IOException | MessagingException e) {
            throw new RuntimeException("Ticket parse olunmadi", e);
        }
    }

    private MimeMessage createMessage(MultipartFile file)
            throws MessagingException, IOException {

        Session session = Session.getDefaultInstance(new Properties());
        return new MimeMessage(session, file.getInputStream());
    }

    private String extractBody(MimeMessage message)
            throws MessagingException, IOException {

        return TicketParser.getTextFromMessage(message)
                .replaceAll("&nbsp;", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private TicketDTO mapToDTO(Ticket ticket) {

        TicketDTO dto = new TicketDTO();

        dto.setPassenger(ticket.getPassenger());
        dto.setSupplier(ticket.getSupplier());
        dto.setTicketNumber(ticket.getTicketNumber());
        dto.setBookingRef(ticket.getBookingRef());
        dto.setIssuanceDate(ticket.getIssuanceDate());
        dto.setPayment(ticket.getPayment());
        dto.setCurrency(ticket.getCurrency());
        dto.setBaseFare(ticket.getBaseFare());
        dto.setTotalAmount(ticket.getTotalAmount());
        dto.setJourneyType(ticket.getJourneyType());
        dto.setRegion(ticket.getRegion());
        dto.setTaxesFeesTotal(ticket.getTaxesFeesTotal());

        dto.setFlights(ticket.getFlights().stream()
                .map(this::mapFlightToDTO)
                .toList());

        return dto;
    }

    private FlightDTO mapFlightToDTO(com.example.parse_task.model.Flight flight) {
        return new FlightDTO(
                flight.getFrom(),
                flight.getTo(),
                flight.getFlightNumber(),
                flight.getDate(),
                flight.getTime()
        );
    }
}
