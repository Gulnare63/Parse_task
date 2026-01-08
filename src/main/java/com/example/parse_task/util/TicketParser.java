package com.example.parse_task.util;

import com.example.parse_task.model.Ticket;
import com.example.parse_task.model.Flight;

import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TicketParser {

    public static String getTextFromMessage(Message message) throws IOException, MessagingException {
        Object content = message.getContent();
        if (content instanceof String) return (String) content;
        if (content instanceof Multipart) {
            Multipart mp = (Multipart) content;
            for (int i = 0; i < mp.getCount(); i++) {
                BodyPart part = mp.getBodyPart(i);
                if (part.isMimeType("text/plain") || part.isMimeType("text/html")) {
                    return (String) part.getContent();
                }
            }
        }
        return "";
    }

    public static String extractSingle(String regex, String text) {
        Matcher m = Pattern.compile(regex).matcher(text);
        return m.find() ? m.group(1).trim() : "TAPILMADI";
    }

    public static String extractJourneyType(String text) {
        Pattern p = Pattern.compile("\\sJ2\\s+\\d+\\s");
        Matcher m = p.matcher(text);
        int count = 0;
        while (m.find()) count++;
        return count > 1 ? "Round trip" : "One way";
    }

    public static String extractRegion(String text) {
        if (text.contains("BAKU") && text.contains("TBILISI")) return "INT";
        return "DOM";
    }

    public static List<Flight> extractFlights(String text) {
        List<Flight> flights = new ArrayList<>();
        Pattern p = Pattern.compile("(BAKU HEYDAR ALI|TBILISI INTERNA)\\s+J2\\s+(\\d+)\\s+[A-Z]*\\s+(\\d{2}[A-Z]{3})\\s+(\\d{4})");
        Matcher m = p.matcher(text);
        while (m.find()) {
            flights.add(new Flight(m.group(1), "J2 " + m.group(2), m.group(3), m.group(4)));
        }
        return flights;
    }

    public static Ticket parseTicket(String body) {
        Ticket ticket = new Ticket();
        ticket.setPassenger(extractSingle("NAME\\s*:\\s*([A-Z/ ]+)", body));
        ticket.setSupplier(extractSingle("ISSUING AIRLINE\\s*:\\s*([A-Z ]+)", body));
        ticket.setTicketNumber(extractSingle("ETKT\\s*((?:\\d+\\s*)+)", body));
        ticket.setBookingRef(extractSingle("AMADEUS\\s*:\\s*(\\w+)", body));
        ticket.setIssuanceDate(extractSingle("DATE\\s*:\\s*(\\d{2} [A-Z]{3} \\d{4})", body));
        ticket.setPayment(extractSingle("PAYMENT\\s*:\\s*(\\w+)", body));
        ticket.setCurrency(extractSingle("TOTAL\\s*:\\s*([A-Z]{3})", body));
        ticket.setBaseFare(extractSingle("AIR FARE\\s*:\\s*([A-Z]{3}\\s*\\d+\\.\\d+)", body));
        ticket.setTotalAmount(extractSingle("TOTAL\\s*:\\s*([A-Z]{3}\\s*\\d+\\.\\d+)", body));
        ticket.setJourneyType(extractJourneyType(body));
        ticket.setRegion(extractRegion(body));
        ticket.setFlights(extractFlights(body));
        return ticket;
    }
}
