package com.ticketWave.ticketWave.dto;

import com.ticketWave.ticketWave.model.Customer;
import com.ticketWave.ticketWave.model.Vendor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TicketDTO {
    private int id;
    private String eventName;
    private double ticketPrice;
    private String eventDescription;
    private LocalDate eventDate;
    private Vendor vendor;
    private Customer customer;
}
