package com.ticketWave.ticketWave.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {
    private int eventId;
    private String eventName;
    private double ticketPrice;
    private String eventDescription;
    private String eventLocation;
    private String eventDateTime;
}
