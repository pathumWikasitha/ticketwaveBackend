package com.ticketWave.ticketWave.controller;

import com.ticketWave.ticketWave.dto.TicketDTO;
import com.ticketWave.ticketWave.dto.UserDTO;
import com.ticketWave.ticketWave.dto.VendorDTO;
import com.ticketWave.ticketWave.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/vendor")
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

    @PostMapping("/register")
    public void registerVendor(@RequestBody VendorDTO vendorDTO) {
        vendorService.registerVendor(vendorDTO);
    }

    @PutMapping("/update/{vendorID}")
    public void updateVendor(@PathVariable int vendorID, @RequestBody VendorDTO vendorDTO) {
        vendorService.updateVendor(vendorID,vendorDTO);
    }

    @PostMapping("/{vendorID}/releaseTickets/{ticketCount}")
    public void releaseTickets(@PathVariable int vendorID, @PathVariable int ticketCount,@RequestBody TicketDTO ticket) {
        vendorService.releaseTickets(vendorID, ticketCount , ticket);
    }
}
