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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TicketParser {

//    private static final Map<String, String> AIRPORT_IATA = Map.of(
//            "BAKU HEYDAR ALI", "GYD",
//            "TBILISI INTERNA", "TBS",
//            "ISTANBUL AIRPORT", "IST"
//            // lazım olarsa əlavə et
//    );

    public static String getTextFromMessage(Message message) throws IOException, MessagingException {
        Object content = message.getContent();
        if (content instanceof String) return (String) content;
        if (content instanceof Multipart) {
            Multipart mp = (Multipart) content;
            for (int i = 0; i < mp.getCount(); i++) {
                BodyPart part = mp.getBodyPart(i);
                if (part.isMimeType("text/plain") || part.isMimeType("text/html")) {
                    Object pc = part.getContent();
                    return pc.toString();
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

    public static List<Flight> extractFlights(String text) {
        List<Flight> flights = new ArrayList<>();

        Pattern p = Pattern.compile(
                "(BAKU HEYDAR ALI|TBILISI INTERNA)\\s+J2\\s+(\\d+)\\s+[A-Z]*\\s+(\\d{2}[A-Z]{3})\\s+(\\d{4})" +
                        ".*?((BAKU HEYDAR ALI|TBILISI INTERNA))\\s+ARRIVAL TIME",
                Pattern.DOTALL
        );

        Matcher m = p.matcher(text);

        while (m.find()) {
            String fromName = m.group(1);
            String flightNumber = "J2 " + m.group(2);
            String date = m.group(3);
            String time = m.group(4);
            String toName = m.group(5);

            // Burada artıq IATA kodunu götürmürük, tam adını veririk
            flights.add(new Flight(fromName, toName, flightNumber, date, time));
        }

        return flights;
    }


    public static String extractRegion(List<Flight> flights) {
        for (Flight f : flights) {
            if (!getCountryCode(f.getFrom()).equals(getCountryCode(f.getTo()))) {
                return "INT"; // beynəlxalq
            }
        }
        return "DOM"; // bütün uçuşlar eyni ölkədədirsə
    }

    private static String getCountryCode(String airport) {
        return switch (airport) {
            case "GYD", "BAKU HEYDAR ALI" -> "AZ";
            case "TBS", "TBILISI INTERNA" -> "GE";
            case "IST", "ISTANBUL AIRPORT" -> "TR";
            default -> "UNKNOWN";
        };
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

        // Flight-ları çıxar və region-u təyin et
        List<Flight> flights = extractFlights(body);
        ticket.setFlights(flights);
        ticket.setRegion(extractRegion(flights));

        ticket.setTaxesFeesTotal(extractTaxesFeesTotal(body));

        return ticket;
    }

    public static String extractTaxesFeesTotal(String text) {
        Pattern p = Pattern.compile("AZN\\s*(\\d+\\.\\d+)\\s*(YQ|YR|KD)");
        Matcher m = p.matcher(text);

        double total = 0.0;

        while (m.find()) {
            total += Double.parseDouble(m.group(1));
        }

        return String.format("AZN %.2f", total);
    }

}
