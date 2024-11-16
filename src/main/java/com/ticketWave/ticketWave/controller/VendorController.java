package com.ticketWave.ticketWave.controller;

import com.ticketWave.ticketWave.dto.UserDTO;
import com.ticketWave.ticketWave.dto.VendorDTO;
import com.ticketWave.ticketWave.model.Ticket;
import com.ticketWave.ticketWave.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1")
@CrossOrigin
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @GetMapping("/{vendorID}")
    public UserDTO getVendor(@PathVariable int vendorID) {
        UserDTO userDTO = vendorService.getVendor(vendorID);
        if (userDTO == null) {
            return null;
        }
        return userDTO;
    }

    @PostMapping("/vendorRegister")
    public void vendorRegister(@RequestBody VendorDTO vendorDTO) {
        vendorService.vendorRegister(vendorDTO);
    }

    @PostMapping("/{vendorID}/releaseTickets")
    public void releaseTickets(@PathVariable int vendorID, @RequestBody Ticket ticket) {
        vendorService.releaseTickets(vendorID, ticket);
    }
}
