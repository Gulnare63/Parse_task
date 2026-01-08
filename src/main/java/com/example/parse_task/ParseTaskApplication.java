package com.example.parse_task;

import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseTaskApplication {

    public static void main(String[] args) {
        String emlPath = "src/main/resources/ticked.eml"; // ticket.eml burada olmalıdır

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props);

        try (FileInputStream fis = new FileInputStream(emlPath)) {

            MimeMessage message = new MimeMessage(session, fis);
            String body = getTextFromMessage(message);


            body = body.replaceAll("&nbsp;", " ").replaceAll("\\s+", " ");

            System.out.println("===== Parse Review =====");


            System.out.println("Passenger: " + extractSingle("NAME\\s*:\\s*([A-Z/ ]+)", body));


            System.out.println("Ticket / ETKT: " + extractSingle("ETKT\\s*(\\d+)", body));


            System.out.println("Booking ref.: " + extractSingle("AMADEUS\\s*:\\s*(\\w+)", body));


            System.out.println("Issuance date: " + extractSingle("DATE\\s*:\\s*(\\d{2} [A-Z]{3} \\d{4})", body));


            System.out.println("Payment: " + extractSingle("PAYMENT\\s*:\\s*(\\w+)", body));


            System.out.println("Total amount: " + extractSingle("TOTAL\\s*:\\s*([A-Z]{2,3}\\s*\\d+\\.\\d+)", body));


            extractFlights(body);

            System.out.println();
            System.out.println();

        } catch (FileNotFoundException e) {
            System.err.println("Fayl tapilmadi: " + e.getMessage());
        } catch (MessagingException e) {
            System.err.println("Email oxuma xətası: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Fayl oxuma xətası: " + e.getMessage());
        }
    }

    // Email body-ni umumi cixariram sonra bu icinden  bir bir lazim olan melumatlari gotururuk
    private static String getTextFromMessage(Message message) throws IOException, MessagingException {
        Object content = message.getContent();

        if (content instanceof String) {
            return (String) content;
        }

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


    private static String extractSingle(String regex, String text) {
        Matcher m = Pattern.compile(regex).matcher(text);
        return m.find() ? m.group(1).trim() : "TAPILMADI";
    }


    private static void extractFlights(String text) {
        System.out.println("\nFlights:");
        Pattern p = Pattern.compile(
                "(BAKU HEYDAR ALI|TBILISI INTERNA)\\s+J2\\s+(\\d+)\\s+[A-Z]*\\s+(\\d{2}[A-Z]{3})\\s+(\\d{4})"
        );

        Matcher m = p.matcher(text);
        int count = 1;
        while (m.find()) {
            System.out.println("Flight " + count + ":");
            System.out.println("  From: " + m.group(1));
            System.out.println("  Flight: J2 " + m.group(2));
            System.out.println("  Date: " + m.group(3));
            System.out.println("  Time: " + m.group(4));
            count++;
        }
    }
}
