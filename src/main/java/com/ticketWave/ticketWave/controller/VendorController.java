package com.ticketWave.ticketWave.controller;

import com.ticketWave.ticketWave.dto.*;
import com.ticketWave.ticketWave.service.EventService;
import com.ticketWave.ticketWave.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/vendor")
@CrossOrigin
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @Autowired
    private EventService eventService;

    @Autowired
    private SystemDTO systemDTO;

    @GetMapping("/{vendorID}")
    public ResponseEntity<VendorDTO> getVendor(@PathVariable int vendorID) {
        VendorDTO vendor = vendorService.getVendor(vendorID);
        if (vendor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vendor);
    }

    @PostMapping("/register")
    public ResponseEntity<VendorDTO> registerVendor(@RequestBody VendorDTO vendorDTO) {
        VendorDTO vendor = vendorService.registerVendor(vendorDTO);
        if (vendor == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(vendor);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateVendor(@RequestBody VendorDTO vendorDTO) {
        VendorDTO vendor = vendorService.updateVendor(vendorDTO);
        if (vendor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("createEvent")
    public ResponseEntity<EventDTO> addEvent(@RequestBody EventDTO eventDTO) {
        EventDTO event = eventService.createEvent(eventDTO);
        if (event != null) {
            return ResponseEntity.ok(event);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/updateEvent")
    public ResponseEntity<EventDTO> updateEvent(@RequestBody EventDTO event) {
        EventDTO updatedEvent = eventService.updateEvent(event);
        if (event != null) {
            return ResponseEntity.ok(updatedEvent);
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteEvent")
    public ResponseEntity<EventDTO> deleteEvent(@RequestBody EventDTO event) {
        EventDTO deletedEvent = eventService.deleteEvent(event);
        if (event != null) {
            return ResponseEntity.ok(deletedEvent);
        }
        return ResponseEntity.internalServerError().build();
    }

    @PostMapping("/{vendorID}/releaseTickets/{ticketCount}")
    public ResponseEntity<String> releaseTickets(@PathVariable int vendorID, @PathVariable int ticketCount, @RequestBody EventDTO eventDTO) {
        if (systemDTO.isRunning()) {
            vendorService.releaseTickets(vendorID, ticketCount, eventDTO);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("System is not running");
    }
}
