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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {



    @Override
    public TicketDTO getTicketFromFile(MultipartFile file) {
        try {
            Session session = Session.getDefaultInstance(System.getProperties());
            MimeMessage message = new MimeMessage(session, file.getInputStream());
            String body = TicketParser.getTextFromMessage(message)
                    .replaceAll("&nbsp;", " ")
                    .replaceAll("\\s+", " ");

            Ticket ticket = TicketParser.parseTicket(body);

            // DTO-ya çevir
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
            dto.setFlights(ticket.getFlights().stream()
                    .map(f -> new FlightDTO(f.getFrom(), f.getTo(), f.getFlightNumber(), f.getDate(), f.getTime()))
                    .toList());

            return dto;

        } catch (IOException | MessagingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public TicketDTO getTicket(String emlPath) {
        try (FileInputStream fis = new FileInputStream(emlPath)) {
            Session session = Session.getDefaultInstance(System.getProperties());
            MimeMessage message = new MimeMessage(session, fis);
            String body = TicketParser.getTextFromMessage(message)
                    .replaceAll("&nbsp;", " ")
                    .replaceAll("\\s+", " ");

            Ticket ticket = TicketParser.parseTicket(body);

            // Convert to DTO
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

            List<FlightDTO> flightDTOs = ticket.getFlights().stream()
                    .map(f -> new FlightDTO(f.getFrom(),f.getTo(), f.getFlightNumber(), f.getDate(), f.getTime()))
                    .toList();
            dto.setFlights(flightDTOs);

            return dto;

        } catch (IOException | MessagingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
