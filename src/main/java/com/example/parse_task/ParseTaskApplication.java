package com.example.parse_task;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class ParseTaskApplication {

    public static void main(String[] args) {
        String emlPath = "src/main/resources/ticked.eml";

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props);

        try (FileInputStream fis = new FileInputStream(emlPath)) {
            MimeMessage message = new MimeMessage(session, fis);
            String body = getTextFromMessage(message)
                    .replaceAll("&nbsp;", " ")
                    .replaceAll("\\s+", " ");

            System.out.println("Passenger: " +
                    TicketExtractor.extractSingle("NAME\\s*:\\s*([A-Z/ ]+)", body));
            System.out.println("Supplier: " +
                    TicketExtractor.extractSingle("ISSUING AIRLINE\\s*:\\s*([A-Z ]+)", body));
            System.out.println("Ticket / ETKT: " +
                    TicketExtractor.extractSingle("ETKT\\s*((?:\\d+\\s*)+)", body));
            System.out.println("Booking ref.: " +
                    TicketExtractor.extractSingle("AMADEUS\\s*:\\s*(\\w+)", body));
            System.out.println("Issuance date: " +
                    TicketExtractor.extractSingle("DATE\\s*:\\s*(\\d{2} [A-Z]{3} \\d{4})", body));
            System.out.println("Payment: " +
                    TicketExtractor.extractSingle("PAYMENT\\s*:\\s*(\\w+)", body));
            System.out.println("Currency: " +
                    TicketExtractor.extractSingle("TOTAL\\s*:\\s*([A-Z]{3})", body));
            System.out.println("Base fare: " +
                    TicketExtractor.extractSingle("AIR FARE\\s*:\\s*([A-Z]{3}\\s*\\d+\\.\\d+)", body));
            System.out.println("Total amount: " +
                    TicketExtractor.extractSingle("TOTAL\\s*:\\s*([A-Z]{3}\\s*\\d+\\.\\d+)", body));

            System.out.println("Journey type: " + TicketExtractor.extractJourneyType(body));
            System.out.println("Region: " + TicketExtractor.extractRegion(body));

            List<Flight> flights = TicketExtractor.extractFlights(body);
            System.out.println("\nFlights:");
            int count = 1;
            for (Flight f : flights) {
                System.out.println("Flight " + count + ":\n" + f);
                count++;
            }

        } catch (FileNotFoundException e) {
            System.err.println("Fayl tapılmadı: " + e.getMessage());
        } catch (MessagingException e) {
            System.err.println("Email oxuma xətası: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Fayl oxuma xətası: " + e.getMessage());
        }
    }

    private static String getTextFromMessage(Message message)
            throws IOException, MessagingException {

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
}
