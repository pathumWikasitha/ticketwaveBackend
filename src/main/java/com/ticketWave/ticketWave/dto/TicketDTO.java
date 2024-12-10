package com.ticketWave.ticketWave.dto;

import com.ticketWave.ticketWave.model.Customer;
import com.ticketWave.ticketWave.model.Event;
import com.ticketWave.ticketWave.model.Vendor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TicketDTO {
    private int id;
    private Event event;
    private Vendor vendor;
    private Customer customer;
}