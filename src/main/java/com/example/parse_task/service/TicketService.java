package com.example.parse_task.service;


import com.example.parse_task.model.dto.TicketDTO;
import org.springframework.web.multipart.MultipartFile;

public interface TicketService {
    TicketDTO getTicketFromFile(MultipartFile file);

    TicketDTO getTicket(String emlPath);
}
