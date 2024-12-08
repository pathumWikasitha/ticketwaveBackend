package com.ticketWave.ticketWave.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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
    private String imageUrl;
}
