package com.example.parse_task;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

public class TicketExtractor {

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
        Pattern p = Pattern.compile(
                "(BAKU HEYDAR ALI|TBILISI INTERNA)\\s+J2\\s+(\\d+)\\s+[A-Z]*\\s+(\\d{2}[A-Z]{3})\\s+(\\d{4})"
        );
        Matcher m = p.matcher(text);
        while (m.find()) {
            flights.add(new Flight(
                    m.group(1),
                    "J2 " + m.group(2),
                    m.group(3),
                    m.group(4)
            ));
        }
        return flights;
    }
}
