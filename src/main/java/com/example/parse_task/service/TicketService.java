package com.example.parse_task.service;


import com.example.parse_task.model.dto.TicketDTO;

public interface TicketService {
    TicketDTO getTicket(String emlPath);
}
