package com.ticketWave.ticketWave.controller;

import com.ticketWave.ticketWave.dto.TicketDTO;
import com.ticketWave.ticketWave.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/ticket")
public class TicketController {

    @Autowired
    TicketService ticketService;

    @GetMapping("/all")
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        List<TicketDTO> tickets = ticketService.getAllTickets();
        if (tickets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/all/{vendorID}")
    public ResponseEntity<List<TicketDTO>> getAllTicketsByVendorID(@PathVariable int vendorID) {
        List<TicketDTO> tickets = ticketService.getAllTicketsByVendorID(vendorID);
        if (tickets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable int id) {
        TicketDTO ticket = ticketService.getTicketById(id);
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ticket);
    }

    @GetMapping("/bookings/{customerID}")
    public ResponseEntity<List<TicketDTO>> getBookings(@PathVariable int customerID) {
        List<TicketDTO> tickets = ticketService.purchasedTickets(customerID);
        if (tickets == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tickets);
    }

}
