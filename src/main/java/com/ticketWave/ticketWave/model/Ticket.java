package com.ticketWave.ticketWave.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String eventName;
    private double ticketPrice;
    private String eventDescription;
    private LocalDate eventDate;
    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

}