package com.example.parse_task.controller;

import com.example.parse_task.model.dto.TicketDTO;
import com.example.parse_task.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/tickets")
public class TicketController {

    private final TicketService ticketService;


    @PostMapping("/ticket")
    public TicketDTO uploadTicket(@RequestParam("file") MultipartFile file) {
        return ticketService.getTicketFromFile(file);
    }
}