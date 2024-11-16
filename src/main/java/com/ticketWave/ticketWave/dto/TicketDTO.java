package com.ticketWave.ticketWave.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TicketDTO {
    private long id;
    private String eventName;
    private double ticketPrice;
    private String eventDescription;
    private LocalDate eventDate;
}
