package com.ticketWave.ticketWave.controller;

import com.ticketWave.ticketWave.dto.EventDTO;
import com.ticketWave.ticketWave.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/event")
@CrossOrigin
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping("/all")
    public ResponseEntity<List<EventDTO>> getEvents() {
        List<EventDTO> events = eventService.getEvents();
        if (events != null) {
            return ResponseEntity.ok(events);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable int id) {
        EventDTO event = eventService.getEventById(id);
        if (event != null) {
            return ResponseEntity.ok(event);
        }
        return ResponseEntity.notFound().build();
    }

}
