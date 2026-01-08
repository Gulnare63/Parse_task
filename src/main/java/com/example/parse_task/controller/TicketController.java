package com.example.parse_task.controller;

import com.example.parse_task.model.dto.TicketDTO;
import com.example.parse_task.service.TicketService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/parse")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }


    @PostMapping("/ticket")
    public TicketDTO uploadTicket(@RequestParam("file") MultipartFile file) {
        return ticketService.getTicketFromFile(file);
    }
}