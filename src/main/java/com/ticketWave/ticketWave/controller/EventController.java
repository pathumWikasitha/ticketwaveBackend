package com.ticketWave.ticketWave.controller;

import com.ticketWave.ticketWave.dto.EventDTO;
import com.ticketWave.ticketWave.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/event")
@CrossOrigin
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping("/1")
    public ResponseEntity<List<EventDTO>> getEvent() {
        EventDTO event = eventService.getEvent();
        if (event != null) {
            return ResponseEntity.ok(Collections.singletonList(event));
        }
        return ResponseEntity.noContent().build();
    }

}
