package com.example.parse_task.controller;

import com.example.parse_task.model.dto.TicketDTO;
import com.example.parse_task.service.TicketService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/parse")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/tic")
    public TicketDTO getTicket() throws IOException {
        // ticked.eml faylı resources qovluğunda olmalıdır
        ClassPathResource resource = new ClassPathResource("ticked.eml");
        String emlPath = resource.getFile().getAbsolutePath();

        return ticketService.getTicket(emlPath);
    }
}
