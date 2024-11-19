package com.ticketWave.ticketWave.controller;

import com.ticketWave.ticketWave.dto.SystemDTO;
import com.ticketWave.ticketWave.dto.TicketDTO;
import com.ticketWave.ticketWave.dto.VendorDTO;
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
    public ResponseEntity<String> registerVendor(@RequestBody VendorDTO vendorDTO) {
        VendorDTO vendor = vendorService.registerVendor(vendorDTO);
        if (vendor == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{vendorID}")
    public ResponseEntity<String> updateVendor(@PathVariable int vendorID, @RequestBody VendorDTO vendorDTO) {
        VendorDTO vendor = vendorService.updateVendor(vendorID, vendorDTO);
        if (vendor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{vendorID}/releaseTickets/{ticketCount}")
    public ResponseEntity<String> releaseTickets(@PathVariable int vendorID, @PathVariable int ticketCount, @RequestBody TicketDTO ticket) {
        if (systemDTO.isRunning()) {
            vendorService.releaseTickets(vendorID, ticketCount, ticket);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("System is not running");
    }
}
